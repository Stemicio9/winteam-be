package com.workonenight.winteambe.repository;

import com.workonenight.winteambe.common.ResourceRepository;
import com.workonenight.winteambe.entity.Skill;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends ResourceRepository<Skill, String> {

}
