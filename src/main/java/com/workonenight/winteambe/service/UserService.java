package com.workonenight.winteambe.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.workonenight.winteambe.dto.UserDTO;
import com.workonenight.winteambe.entity.User;
import com.workonenight.winteambe.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
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
            return new UserDTO(firebaseToken.getEmail());
        }
        return null;
    }

    public UserDTO registerUser(HttpServletRequest request) {
        User user = firebaseService.getMinimalUser(request);
        userRepository.save(user);
        return user.toDTO();
    }
}
