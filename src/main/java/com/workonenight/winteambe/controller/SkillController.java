package com.workonenight.winteambe.controller;

import com.workonenight.winteambe.dto.SkillDTO;
import com.workonenight.winteambe.service.SkillService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/skill")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    /**
     * Get all skills
     * @return List<SkillDTO> List of skills
     */
    @GetMapping(value = "/list/all")
    public List<SkillDTO> getAllSkill() {
        return skillService.getAllSkill();
    }

    /**
     * Get skill by id
     * @param id Skill id
     * @return SkillDTO Skill
     */
    @GetMapping(value = "/list/{id}")
    public SkillDTO getSkillById(@PathVariable("id") String id) {
        return skillService.getSkillById(id);
    }

    /**
     * Create skill
     * @param skillDTO Skill
     * @return SkillDTO Created skill
     */
    @PostMapping(value = "/create")
    public SkillDTO createSkill(@RequestBody SkillDTO skillDTO) {
        return skillService.createSkill(skillDTO);
    }

    /**
     * Update skill
     * @param skillDTO Skill
     * @return SkillDTO Updated skill
     */
    @PostMapping(value = "/update")
    public SkillDTO updateSkill(@RequestBody SkillDTO skillDTO) {
        return skillService.updateSkill(skillDTO);
    }
}
