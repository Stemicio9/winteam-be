package com.workonenight.winteambe.entity.interfaces;

public abstract class DataTransferObject {
    public abstract DataTransferObject createBaseDTO();

    public void setExtraField(String fieldName, String fieldValue) {
        throw new UnsupportedOperationException();
    }

    public String getExtraField(String fieldName) {
        throw new UnsupportedOperationException();
    }
}
