package com.workonenight.winteambe.entity;

import com.workonenight.winteambe.dto.AdvertisementDTO;
import com.workonenight.winteambe.entity.interfaces.DataTransferObject;
import com.workonenight.winteambe.entity.interfaces.Transferrable;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "advertisements")
public class Advertisement extends DataTransferObject implements Serializable, Transferrable {

    @Id
    private String id;
    private String description;
    private LocalDateTime date;
    private String hourSlot;
    @DBRef
    private Skill skill;
    private Double payment;
    private String position;
    @DBRef
    private User publisherUser;
    @DBRef
    private List<User> candidateUserList = new ArrayList<>();
    @DBRef
    private User matchedUser;


    @Override
    public DataTransferObject asDTO() {
        return new AdvertisementDTO();
    }

    @Override
    public DataTransferObject createBaseDTO() {
        return new Advertisement();
    }
}
