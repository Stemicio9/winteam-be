package com.workonenight.winteambe.repository;

import com.workonenight.winteambe.entity.Skill;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SkillRepository extends MongoRepository<Skill, String> {
}
