package com.workonenight.winteambe.entity;

import com.workonenight.winteambe.dto.CategoryDTO;
import com.workonenight.winteambe.entity.interfaces.DataTransferObject;
import com.workonenight.winteambe.entity.interfaces.Transferrable;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document(collection = "categories")
public class Category extends DataTransferObject implements Serializable, Transferrable {

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


    @Override
    public DataTransferObject createBaseDTO() {
        return new Category();
    }

    @Override
    public DataTransferObject asDTO() {
        return new CategoryDTO();
    }
}
