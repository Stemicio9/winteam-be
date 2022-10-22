package com.workonenight.winteambe.entity;

import com.workonenight.winteambe.dto.SkillDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Document(collection = "skills")
public class Skill implements Serializable {

    @Id
    private String id;
    private String name;
    private String imageLink;
    private String hexColorText;
    private String hexColorBackground;

    public SkillDTO toDTO(){
        SkillDTO skillDTO = new SkillDTO();
        skillDTO.setId(this.id);
        skillDTO.setName(this.name);
        skillDTO.setImageLink(this.imageLink);
        skillDTO.setHexColorText(this.hexColorText);
        skillDTO.setHexColorBackground(this.hexColorBackground);
        return skillDTO;
    }

    public Skill toUpdateEntity(SkillDTO skillDTO){
        this.name = skillDTO.getName();
        this.imageLink = skillDTO.getImageLink();
        this.hexColorText = skillDTO.getHexColorText();
        this.hexColorBackground = skillDTO.getHexColorBackground();
        return this;
    }

}
