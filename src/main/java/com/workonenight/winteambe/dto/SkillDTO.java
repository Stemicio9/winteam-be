package com.workonenight.winteambe.dto;

import com.workonenight.winteambe.entity.Skill;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SkillDTO {
    private String id;
    private String name;
    private String imageLink;
    private String hexColor;

    public Skill toEntity(){
        Skill skill = new Skill();
        skill.setId(this.id);
        skill.setName(this.name);
        skill.setImageLink(this.imageLink);
        skill.setHexColor(this.hexColor);
        return skill;
    }
}
