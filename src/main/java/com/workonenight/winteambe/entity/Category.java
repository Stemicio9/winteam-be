package com.workonenight.winteambe.entity;

import com.workonenight.winteambe.dto.CategoryDTO;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "categories")
public class Category {

    @Id
    private String id;
    private String name;
    private String imageLink;

    public CategoryDTO toDTO(){
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(this.id);
        categoryDTO.setName(this.name);
        categoryDTO.setImageLink(this.imageLink);
        return categoryDTO;
    }

    public Category toUpdateEntity(CategoryDTO categoryDTO){
        this.name = categoryDTO.getName();
        this.imageLink = categoryDTO.getImageLink();
        return this;
    }


}
