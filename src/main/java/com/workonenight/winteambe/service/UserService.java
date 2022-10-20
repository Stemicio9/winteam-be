package com.workonenight.winteambe.service;

import com.google.firebase.auth.FirebaseToken;
import com.workonenight.winteambe.dto.CanIDTO;
import com.workonenight.winteambe.dto.SkillDTO;
import com.workonenight.winteambe.dto.SubscriptionDTO;
import com.workonenight.winteambe.dto.UserDTO;
import com.workonenight.winteambe.entity.User;
import com.workonenight.winteambe.repository.UserRepository;
import com.workonenight.winteambe.service.other.FirebaseService;
import com.workonenight.winteambe.utils.UserType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final FirebaseService firebaseService;
    private final SubscriptionService subscriptionService;
    private final SkillService skillService;

    public UserService(UserRepository userRepository, FirebaseService firebaseService, SubscriptionService subscriptionService, SkillService skillService) {
        this.userRepository = userRepository;
        this.firebaseService = firebaseService;
        this.subscriptionService = subscriptionService;
        this.skillService = skillService;
    }

    public List<UserDTO> getAllUser() {
        log.info("Get all user");
        return userRepository.findAll().stream().map(User::toDTO).collect(Collectors.toList());
    }

    public UserDTO getUserById(String id) {
        Optional<User> opt = userRepository.findById(id);
        if(opt.isPresent()){
            UserDTO userDTO = opt.get().toDTO();
            userDTO.setSkillList(generateSkillDTOList(userDTO.getSkillIds()));
            return userDTO;
        }
        return null;
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = userDTO.toEntity();
        log.info("Creating user: " + user.getEmail());
        return userRepository.save(user).toDTO();
    }

    public UserDTO updateUser(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getId()).orElse(null);
        if (user != null) {
            user = user.toUpdateEntity(userDTO);
            return userRepository.save(user).toDTO();
        }
        return null;
    }

    public UserDTO getMe(HttpServletRequest request) {
        FirebaseToken firebaseToken = firebaseService.getFirebaseToken(request);
        if (firebaseToken != null) {
            log.info("Requested info for user: {}", firebaseToken.getUid());
            UserDTO userDTO = getUserById(firebaseToken.getUid());
            if (userDTO != null) {
                log.info("User found: {}", userDTO);
                return userDTO;
            }
            log.error("User not found for email: {}", firebaseToken.getEmail());
        }
        log.error("Token not found in request");
        return null;
    }

    private List<SkillDTO> generateSkillDTOList(List<String> skillIds) {
        log.info("GET ALL SKILL: {}",skillService.getAllSkill());
        List<SkillDTO> res = new ArrayList<>();
        for(String id: skillIds){
            SkillDTO skillDTO = skillService.getSkillById(id);
            if(skillDTO != null){
                res.add(skillDTO);
            }
        }
        return res;
    }

    public UserDTO registerUser(HttpServletRequest request, String role) {
        User user = firebaseService.getMinimalUser(request);
        if (user != null) {
            if (existUserByEmail(user.getEmail())) {
                log.error("User already exist: {}", user.getEmail());
                return null;
            }
            user.setRoleId(UserType.isRole(role) ? role : UserType.LAVORATORE);
            log.info("Registering user: " + user.getEmail());
            return userRepository.save(user).toDTO();
        }
        log.error("Error during user registration");
        return null;
    }

    public Page<UserDTO> getPageFiltered(Query query, Pageable pageable) {
        return userRepository.findAll(query, pageable).map(User::toDTO);
    }

    public List<UserDTO> getAllFiltered(Query query) {
        return userRepository.findAll(query).stream().map(User::toDTO).collect(Collectors.toList());
    }

    private boolean existUserByEmail(String email) {
        return userRepository.findUserByEmail(email).isPresent();
    }

    private User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElse(null);
    }

    public CanIDTO canI(HttpServletRequest request, String what) {
        FirebaseToken firebaseToken = firebaseService.getFirebaseToken(request);
        CanIDTO canIDTO = new CanIDTO();
        if (firebaseToken != null) {
            log.info("Requested info for user: {}", firebaseToken.getUid());
            UserDTO userDTO = getUserById(firebaseToken.getUid());
            String subscriptionId = userDTO.getSubscriptionId();
            if (subscriptionId == null) {
                log.info("User {} has no subscription", userDTO.getEmail());
                canIDTO.setResponse(false);
            } else {
                SubscriptionDTO subscription = subscriptionService.getSubscriptionById(subscriptionId);
                switch (what) {
                    case "search":
                        log.info("Can user {} search? {}", userDTO.getEmail(), subscription.isSearchEnabled());
                        canIDTO.setResponse(subscription.isSearchEnabled());
                    case "createAdvertisement":
                        boolean res = subscription.isCreateAdvertisementEnabled() && subscription.getNumAnnunci() > userDTO.getContatoreAnnunci();
                        log.info("Can user {} create advertisement? {}", userDTO.getEmail(), res);
                        canIDTO.setResponse(res);
                    default:
                        log.error("Unknown what: {}", what);
                        canIDTO.setResponse(false);
                }
            }
            return canIDTO;
        }
        log.error("Token not found in request");
        return null;
    }
}
