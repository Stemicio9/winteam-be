package com.workonenight.winteambe.dto;

import com.workonenight.winteambe.entity.Category;
import com.workonenight.winteambe.entity.interfaces.DataTransferObject;
import lombok.Data;

@Data
public class CategoryDTO extends DataTransferObject {
    private String id;
    private String name;
    private String imageLink;

    public Category toEntity(){
        Category category = new Category();
        category.setId(this.id);
        category.setName(this.name);
        category.setImageLink(this.imageLink);
        return category;
    }

    @Override
    public DataTransferObject createBaseDTO(){
        return new CategoryDTO();
    }
}
