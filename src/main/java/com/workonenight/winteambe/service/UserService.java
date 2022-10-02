package com.workonenight.winteambe.service;

import com.google.firebase.auth.FirebaseToken;
import com.workonenight.winteambe.dto.UserDTO;
import com.workonenight.winteambe.entity.User;
import com.workonenight.winteambe.repository.UserRepository;
import com.workonenight.winteambe.service.other.FirebaseService;
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
            return firebaseService.getMinimalUser(request).toDTO();
        }
        return null;
    }

    public UserDTO registerUser(HttpServletRequest request) {
        User user = firebaseService.getMinimalUser(request);
        log.info("Registering user: " + user.getEmail());
        userRepository.save(user);
        return user.toDTO();
    }

    public Page<UserDTO> getPageFiltered(Query query, Pageable pageable) {
        return userRepository.findAll(query, pageable).map(User::toDTO);
    }

    public List<UserDTO> getAllFiltered(Query query) {
        return userRepository.findAll(query).stream().map(User::toDTO).collect(Collectors.toList());
    }
}
