package com.workonenight.winteambe.service;

import com.workonenight.winteambe.dto.CanIDTO;
import com.workonenight.winteambe.dto.response.SubscriptionResponse;
import com.workonenight.winteambe.entity.Skill;
import com.workonenight.winteambe.entity.User;
import com.workonenight.winteambe.enums.FirebaseRequestElement;
import com.workonenight.winteambe.exception.RegistrationGenericErrorException;
import com.workonenight.winteambe.exception.UserAlreadyExistsException;
import com.workonenight.winteambe.exception.UserEmailNotFoundException;
import com.workonenight.winteambe.exception.UserNotAuthenticatedException;
import com.workonenight.winteambe.repository.SkillRepository;
import com.workonenight.winteambe.repository.UserRepository;
import com.workonenight.winteambe.utils.UserType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

import static com.workonenight.winteambe.utils.Utils.composeSubscriptionResponse;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final AuthenticationService authenticationService;

    private final UserPermissionService userPermissionService;

    private final SkillRepository skillRepository;


    public UserService(UserRepository userRepository, AuthenticationService authenticationService, UserPermissionService userPermissionService, SkillRepository skillRepository) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.userPermissionService = userPermissionService;
        this.skillRepository = skillRepository;
    }


    // This method is used to retrieve all users from db
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    // todo add exception User not found, throw in signature and catch in controller returning correct error message and status
    public User findUserById(String id) throws RuntimeException {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Save user receives the user as it needs to be saved in DB
    public User saveUser(User user) {
        return userRepository.save(user);
    }


    public User createUser(User user) {
        log.info("Creating user: " + user.getEmail());
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        log.info("Updating user: " + user.getEmail());
        if (user != null) {
            return userRepository.save(user);
        }
        return null;
    }


    public User registerUser(HttpServletRequest request, String role) throws UserAlreadyExistsException, RegistrationGenericErrorException {
        User user = authenticationService.getMinimalUser(request);
        if (user != null) {
            if (existUserByEmail(user.getEmail())) {
                log.error("User already exist: {}", user.getEmail());
                throw new UserAlreadyExistsException();
            }
            user.setRoleId(UserType.isRole(role) ? role : UserType.LAVORATORE);
            log.info("Registering user: " + user.getEmail());
            return userRepository.save(user);

        }
        log.error("Error during user registration");
        throw new RegistrationGenericErrorException();
    }




    public Page<User> getPageFiltered(HttpServletRequest request, Query query, Pageable pageable) {
        String myId = authenticationService.myInfo(request, FirebaseRequestElement.ID);
        query.addCriteria(Criteria.where("id").ne(myId)).addCriteria(Criteria.where("roleId").ne(UserType.ADMIN));
        return getAllFiltered(query, pageable);
    }

    public Page<User> getAllFiltered(Query query, Pageable pageable) {
        log.info("Get all user filtered");
        return userRepository.findAll(query, pageable);
    }

    public CanIDTO canI(HttpServletRequest request, String what) throws UserNotAuthenticatedException {
        String myId = authenticationService.myInfo(request, FirebaseRequestElement.ID);
        CanIDTO canIDTO = new CanIDTO();

        log.info("Requested info for user: {}", myId);
        User userDTO = findUserById(myId);

        if (!userPermissionService.hasUserSubscription(userDTO)) {
            //TODO controlliamo se l'utente ha un nome sub vuoto o null questo vuol dire che non ha ancora scelto una sottoscrizione
            log.info("User {} has no subscription", userDTO.getEmail());
            canIDTO.setResponse(false);
        } else {
            switch (what) {
                case "search":
                    log.info("Can user {} search? {}", userDTO.getEmail(), userDTO.isSearchEnabled());
                    canIDTO.setResponse(userDTO.isSearchEnabled());
                    break;
                case "createAdvertisement":
                    boolean res = userDTO.isCreateAdvertisementEnabled() && userDTO.getAdvertisementLeft() > 0;
                    log.info("Can user {} create advertisement? {}", userDTO.getEmail(), res);
                    canIDTO.setResponse(res);
                    break;
                default:
                    log.error("Unknown what: {}", what);
                    canIDTO.setResponse(false);
                    break;
            }
        }
        return canIDTO;
    }

    public SubscriptionResponse mySubscription(HttpServletRequest request) throws UserNotAuthenticatedException {
        String myId = authenticationService.myInfo(request, FirebaseRequestElement.ID);

        log.info("Requested info for user: {}", myId);
        User userDTO = findUserById(myId);
        if (userDTO != null) {
            log.info("User found: {}", userDTO);
            return composeSubscriptionResponse(userDTO);
        }
        log.error("User not found for email: {}", myId);

        log.error("Token not found in request");
        throw new UserNotAuthenticatedException();
    }

    public Page<User> searchUser(String search, Pageable pageable) {
        log.info("Searching user with skill contains param: {}", search);
        Optional<Skill> skill = skillRepository.findByName(search);
        String skillSearch = "";
        if(skill.isPresent()){
            skillSearch = skill.get().getId();
        }
        return userRepository.findAllByRoleIdAndFirstNameContainsOrRoleIdAndLastNameContainsOrRoleIdAndSkillList_Id(UserType.LAVORATORE,
                search, UserType.LAVORATORE, search, UserType.LAVORATORE, skillSearch, pageable);
    }



    private boolean existUserByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

/*    public boolean hasUserSubscription(HttpServletRequest request) {
        FirebaseToken firebaseToken = firebaseService.getFirebaseToken(request);
        if (firebaseToken != null) {
            log.info("Requested info for user: {}", firebaseToken.getUid());
            CompanyDTO userDTO = (CompanyDTO) getUserById(firebaseToken.getUid());
            if (userDTO != null) {
                log.info("User found: {}", userDTO);
                return StringUtils.hasLength(userDTO.getSubscriptionName());
            }
            log.error("User not found for email: {}", firebaseToken.getEmail());
        }
        log.error("Token not found in request please update your app");
        return false;
    } */

    public User getMe(HttpServletRequest request) throws UserNotAuthenticatedException, UserEmailNotFoundException {
        String id = authenticationService.myInfo(request, FirebaseRequestElement.ID);
        Optional<User> userDTO = userRepository.findById((id));
        if (userDTO.isPresent()) {
            log.info("User found: {}", userDTO);
            return userDTO.get();
        }
        log.error("User not found for email: {}", authenticationService.myInfo(request, FirebaseRequestElement.EMAIL));
        throw new UserEmailNotFoundException();
    }

}
