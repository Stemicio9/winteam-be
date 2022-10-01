package com.workonenight.winteambe.controller;

import com.workonenight.winteambe.service.UserService;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
}
