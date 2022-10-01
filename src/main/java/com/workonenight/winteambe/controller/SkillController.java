package com.workonenight.winteambe.controller;

import com.workonenight.winteambe.service.SkillService;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/category")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }
}
