package com.workonenight.winteambe.dto;

import com.workonenight.winteambe.entity.interfaces.DataTransferObject;
import lombok.Data;

@Data
public class CanIDTO extends DataTransferObject {

    private boolean response;

    @Override
    public DataTransferObject createBaseDTO(){
        return new CanIDTO();
    }
}
