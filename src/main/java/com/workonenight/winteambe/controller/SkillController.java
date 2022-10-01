package com.workonenight.winteambe.controller;

import com.workonenight.winteambe.dto.SkillDTO;
import com.workonenight.winteambe.service.SkillService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController(value = "/skill")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping(value = "/list/all")
    public List<SkillDTO> getAllSkill() {
        return skillService.getAllSkill();
    }
    @GetMapping(value = "/list/{id}")
    public SkillDTO getSkyllById(@PathVariable("id") String id) {
        return skillService.getSkillById(id);
    }
    @PostMapping(value = "/create")
    public SkillDTO createSkill(@RequestBody SkillDTO skillDTO) {
        return skillService.createSkill(skillDTO);
    }
    @PostMapping(value = "/update")
    public SkillDTO updateSkill(@RequestBody SkillDTO skillDTO) {
        return skillService.updateSkill(skillDTO);
    }
}
