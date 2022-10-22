package com.workonenight.winteambe.controller;

import com.workonenight.winteambe.common.FilterCondition;
import com.workonenight.winteambe.common.GenericFilterCriteriaBuilder;
import com.workonenight.winteambe.dto.SkillDTO;
import com.workonenight.winteambe.service.SkillService;
import com.workonenight.winteambe.service.other.FilterBuilderService;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/skill")
public class SkillController {

    private final SkillService skillService;
    private final FilterBuilderService filterBuilderService;

    public SkillController(SkillService skillService, FilterBuilderService filterBuilderService) {
        this.skillService = skillService;
        this.filterBuilderService = filterBuilderService;
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

    /**
     * @param filterOr  string filter or conditions
     * @param filterAnd string filter and conditions
     * @return list of SkillDTO
     */
    @GetMapping("/list/filter")
    public ResponseEntity<List<SkillDTO>> getAllSearchCriteria(
            @RequestParam(value = "filterOr", required = false) String filterOr,
            @RequestParam(value = "filterAnd", required = false) String filterAnd) {

        GenericFilterCriteriaBuilder filterCriteriaBuilder = new GenericFilterCriteriaBuilder();

        List<FilterCondition> andConditions = filterBuilderService.createFilterCondition(filterAnd);
        List<FilterCondition> orConditions = filterBuilderService.createFilterCondition(filterOr);

        Query query = filterCriteriaBuilder.addCondition(andConditions, orConditions);
        List<SkillDTO> skillDTOList = skillService.getAllFiltered(query);

        return new ResponseEntity<>(skillDTOList, HttpStatus.OK);
    }
}
