package com.workonenight.winteambe.controller;

import com.workonenight.winteambe.dto.UserDTO;
import com.workonenight.winteambe.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/list/all")
    public List<UserDTO> getAllUser() {
        return userService.getAllUser();
    }

    @GetMapping(value = "/list/{id}")
    public UserDTO getUserById(@PathVariable("id") String id) {
        return userService.getUserById(id);
    }

    @PostMapping(value = "/create")
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @GetMapping(value = "/register")
    public UserDTO registerUser(HttpServletRequest request) {
        return userService.registerUser(request);
    }

    @PostMapping(value = "/update")
    public UserDTO updateUser(@RequestBody UserDTO userDTO) {
        return userService.updateUser(userDTO);
    }

    @GetMapping(value="/me")
    public UserDTO getMe(HttpServletRequest request) {
        return userService.getMe(request);
    }
}
