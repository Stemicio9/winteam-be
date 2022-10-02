package com.workonenight.winteambe.service;

import com.google.firebase.auth.FirebaseToken;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final FirebaseService firebaseService;

    public UserService(UserRepository userRepository, FirebaseService firebaseService) {
        this.userRepository = userRepository;
        this.firebaseService = firebaseService;
    }

    public List<UserDTO> getAllUser() {
        return userRepository.findAll().stream().map(User::toDTO).collect(Collectors.toList());
    }

    public UserDTO getUserById(String id) {
        return userRepository.findById(id).orElseThrow().toDTO();
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
            User user = getUserByEmail(firebaseToken.getEmail());
            if (user != null) {
                return user.toDTO();
            }
            log.error("User not found for email: {}", firebaseToken.getEmail());
        }
        log.error("Token not found in request");
        return null;
    }

    public UserDTO registerUser(HttpServletRequest request, String role) {
        User user = firebaseService.getMinimalUser(request);
        if (user != null) {
            if(existUserByEmail(user.getEmail())) {
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

}
