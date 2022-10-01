package com.workonenight.winteambe.entity;

import com.workonenight.winteambe.dto.SkillDTO;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "skills")
public class Skill {

    @Id
    private String id;
    private String name;
    private String imageLink;

    public SkillDTO toDTO(){
        SkillDTO skillDTO = new SkillDTO();
        skillDTO.setId(this.id);
        skillDTO.setName(this.name);
        skillDTO.setImageLink(this.imageLink);
        return skillDTO;
    }

}
