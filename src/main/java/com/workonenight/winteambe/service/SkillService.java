package com.workonenight.winteambe.service;

import com.workonenight.winteambe.dto.SkillDTO;
import com.workonenight.winteambe.entity.Skill;
import com.workonenight.winteambe.repository.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public List<SkillDTO> getAllSkill() {
        return skillRepository.findAll().stream().map(Skill::toDTO).collect(Collectors.toList());
    }

    public SkillDTO getSkillById(String id) {
        return skillRepository.findById(id).orElseThrow().toDTO();
    }
    public SkillDTO createSkill(SkillDTO skillDTO) {
        Skill skill = skillDTO.toEntity();
        return skillRepository.save(skill).toDTO();
    }

    public SkillDTO updateSkill(SkillDTO skillDTO) {
        Skill skill = skillRepository.findById(skillDTO.getId()).orElse(null);
        if (skill != null) {
            skill = skill.toUpdateEntity(skillDTO);
            return skillRepository.save(skill).toDTO();
        }
        return null;
    }
}
