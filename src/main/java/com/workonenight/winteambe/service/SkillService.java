package com.workonenight.winteambe.service;

import com.workonenight.winteambe.dto.SkillDTO;
import com.workonenight.winteambe.entity.Skill;
import com.workonenight.winteambe.repository.SkillRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public List<SkillDTO> getAllSkill() {
        log.info("Get All Skills");
        return skillRepository.findAll().stream().map(Skill::toDTO).collect(Collectors.toList());
    }

    public SkillDTO getSkillById(String id) {
        log.info("Searching skill with id: {}", id);
        Optional<Skill> opt = skillRepository.findById(id);
        return opt.map(Skill::toDTO).orElse(null);
    }
    public SkillDTO createSkill(SkillDTO skillDTO) {
        log.info("Creating skill: {}", skillDTO.getName());
        Skill skill = skillDTO.toEntity();
        return skillRepository.save(skill).toDTO();
    }

    public SkillDTO updateSkill(SkillDTO skillDTO) {
        log.info("Updating skill: {}", skillDTO.getName());
        Skill skill = skillRepository.findById(skillDTO.getId()).orElse(null);
        if (skill != null) {
            skill = skill.toUpdateEntity(skillDTO);
            return skillRepository.save(skill).toDTO();
        }
        return null;
    }

    public List<SkillDTO> getAllFiltered(Query query) {
        log.info("Get All Skills Filtered");
        return skillRepository.findAll(query).stream().map(Skill::toDTO).collect(Collectors.toList());
    }

}
