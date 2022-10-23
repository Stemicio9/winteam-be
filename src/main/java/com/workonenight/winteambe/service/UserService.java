package com.workonenight.winteambe.service;

import com.google.firebase.auth.FirebaseToken;
import com.workonenight.winteambe.dto.*;
import com.workonenight.winteambe.entity.User;
import com.workonenight.winteambe.repository.UserRepository;
import com.workonenight.winteambe.service.other.FirebaseService;
import com.workonenight.winteambe.utils.UserType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    public List<BaseUserDTO> getAllUser(HttpServletRequest request) {
        log.info("Get all user");
        return hasUserSubscription(request) ?
                userRepository.findAll().stream().map(u -> this.finalizeUserDTO(u.toDTO())).collect(Collectors.toList()) :
                userRepository.findAll().stream().map(u -> this.finalizeUserDTO(u.toDTOAnonymous())).collect(Collectors.toList());
    }

    public BaseUserDTO getUserById(String id) {
        log.info("Searching user for id: {}", id);
        Optional<User> opt = userRepository.findById(id);
        return opt.map(u -> this.finalizeUserDTO(u.toDTO())).orElse(null);
    }

    public BaseUserDTO createUser(UserDTO userDTO) {
        log.info("Creating user: " + userDTO.getEmail());
        User user = userDTO.toEntity();
        UserDTO result = userRepository.save(user).toDTO();
        return finalizeUserDTO(result);
    }

    public BaseUserDTO updateUser(UserDTO userDTO) {
        log.info("Updating user: " + userDTO.getEmail());
        User user = userRepository.findById(userDTO.getId()).orElse(null);
        if (user != null) {
            user = user.toUpdateEntity(userDTO);
            UserDTO updatedUser = userRepository.save(user).toDTO();
            return finalizeUserDTO(updatedUser);
        }
        return null;
    }

    public BaseUserDTO getMe(HttpServletRequest request) {
        FirebaseToken firebaseToken = firebaseService.getFirebaseToken(request);
        if (firebaseToken != null) {
            log.info("Requested info for user: {}", firebaseToken.getUid());
            BaseUserDTO userDTO = getUserById(firebaseToken.getUid());
            if (userDTO != null) {
                log.info("User found: {}", userDTO);
                return userDTO;
            }
            log.error("User not found for email: {}", firebaseToken.getEmail());
        }
        log.error("Token not found in request");
        return null;
    }

    public BaseUserDTO registerUser(HttpServletRequest request, String role) {
        User user = firebaseService.getMinimalUser(request);
        if (user != null) {
            if (existUserByEmail(user.getEmail())) {
                log.error("User already exist: {}", user.getEmail());
                return null;
            }
            user.setRoleId(UserType.isRole(role) ? role : UserType.LAVORATORE);
            log.info("Registering user: " + user.getEmail());
            UserDTO result = userRepository.save(user).toDTO();
            return finalizeUserDTO(result);
        }
        log.error("Error during user registration");
        return null;
    }

    //TODO fix this method
    public Page<BaseUserDTO> getPageFiltered(HttpServletRequest request, Query query, Pageable pageable) {
        query.addCriteria(Criteria.where("id").ne(firebaseService.getMinimalUser(request).getId())).addCriteria(Criteria.where("roleId").ne(UserType.ADMIN));
        return hasUserSubscription(request) ?
                userRepository.findAll(query, pageable).map(u -> this.finalizeUserDTO(u.toDTO())) :
                userRepository.findAll(query, pageable).map(u -> this.finalizeUserDTO(u.toDTOAnonymous()));

    }

    public List<UserDTO> getAllFiltered(Query query) {
        log.info("Get all user filtered");
        return userRepository.findAll(query).stream().map(User::toDTO).peek(this::finalizeUserDTO).collect(Collectors.toList());
    }

    public CanIDTO canI(HttpServletRequest request, String what) {
        FirebaseToken firebaseToken = firebaseService.getFirebaseToken(request);
        CanIDTO canIDTO = new CanIDTO();
        if (firebaseToken != null) {
            log.info("Requested info for user: {}", firebaseToken.getUid());
            CompanyDTO userDTO = (CompanyDTO) getUserById(firebaseToken.getUid());
            String subscriptionId = userDTO.getSubscriptionId();
            if (!StringUtils.hasLength(subscriptionId)) {
                log.info("User {} has no subscription", userDTO.getEmail());
                canIDTO.setResponse(false);
            } else {
                SubscriptionDTO subscription = subscriptionService.getSubscriptionById(subscriptionId);
                switch (what) {
                    case "search":
                        log.info("Can user {} search? {}", userDTO.getEmail(), subscription.isSearchEnabled());
                        canIDTO.setResponse(subscription.isSearchEnabled());
                        break;
                    case "createAdvertisement":
                        boolean res = subscription.isCreateAdvertisementEnabled() && subscription.getNumAnnunci() > userDTO.getContatoreAnnunci();
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
        log.error("Token not found in request");
        return null;
    }


    private BaseUserDTO finalizeUserDTO(UserDTO userDTO) {
        if (userDTO.getSkillIds() == null) {
            userDTO.setSkillIds(new ArrayList<>());
            userDTO.setSkillList(new ArrayList<>());
        } else {
            List<SkillDTO> skillList = generateSkillDTOList(userDTO.getSkillIds());
            userDTO.setSkillList(skillList);
        }
        return mapUserByRole(userDTO);
    }

    private BaseUserDTO mapUserByRole(UserDTO userDTO) {
        switch (userDTO.getRoleId()) {
            case UserType.LAVORATORE:
            case UserType.ADMIN:
                return LavoratoreDTO.fromUserDTOtoLavoratoreDTO(userDTO);
            case UserType.DATORE:
                return CompanyDTO.fromUserDTOtoCompanyDTO(userDTO);
            default:
                log.error("Unknown role: {}", userDTO.getRoleId());
                return null;
        }
    }

    private List<SkillDTO> generateSkillDTOList(List<String> skillIds) {
        List<SkillDTO> res = new ArrayList<>();
        for (String id : skillIds) {
            SkillDTO skillDTO = skillService.getSkillById(id);
            if (skillDTO != null) {
                res.add(skillDTO);
            }
        }
        return res;
    }

    private boolean existUserByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private boolean hasUserSubscription(HttpServletRequest request) {
        FirebaseToken firebaseToken = firebaseService.getFirebaseToken(request);
        if (firebaseToken != null) {
            log.info("Requested info for user: {}", firebaseToken.getUid());
            CompanyDTO userDTO = (CompanyDTO) getUserById(firebaseToken.getUid());
            if (userDTO != null) {
                log.info("User found: {}", userDTO);
                String subscriptionId = userDTO.getSubscriptionId();
                return StringUtils.hasLength(subscriptionId) && subscriptionService.existsById(subscriptionId);
            }
            log.error("User not found for email: {}", firebaseToken.getEmail());
        }
        log.error("Token not found in request");
        return false;
    }
}
