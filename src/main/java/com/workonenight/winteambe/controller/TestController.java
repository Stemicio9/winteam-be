package com.workonenight.winteambe.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/public")
public class TestController {

    @RequestMapping(value = "/test")
    public String test() {
        return "test";
    }
}
