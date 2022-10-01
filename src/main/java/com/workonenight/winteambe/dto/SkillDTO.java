package com.workonenight.winteambe.dto;

import com.workonenight.winteambe.entity.Skill;
import lombok.Data;

@Data
public class SkillDTO {
    private String id;
    private String name;
    private String imageLink;

    public Skill toEntity(){
        Skill skill = new Skill();
        skill.setId(this.id);
        skill.setName(this.name);
        skill.setImageLink(this.imageLink);
        return skill;
    }
}
