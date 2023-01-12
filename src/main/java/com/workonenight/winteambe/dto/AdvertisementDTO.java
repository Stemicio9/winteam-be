package com.workonenight.winteambe.dto;

import com.workonenight.winteambe.entity.Advertisement;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class AdvertisementDTO {
    private String id;
    private String description;
    private String date;
    private String hourSlot;

    //skill id matched with skill DTO
    private String skillId;
    private SkillDTO skillDTO;

    private Double payment;

    private String position;
    private String publisherUserId;
    private BaseUserDTO publisherUserDTO;
    private List<String> candidateUserList;

    private String matchedUserId;
    private String advertisementStatus;

    //convert dto to entity
    public Advertisement toEntity() {
        Advertisement advertisement = new Advertisement();
        advertisement.setId(this.id);
        advertisement.setDescription(this.description);
        advertisement.setDate(LocalDateTime.parse(this.date));
        advertisement.setHourSlot(this.hourSlot);
        advertisement.setSkillId(this.skillId);
        advertisement.setPayment(this.payment);
        advertisement.setPosition(this.position);
        advertisement.setPublisherUserId(this.publisherUserId);
        advertisement.setCandidateUserList((this.candidateUserList != null) ? this.candidateUserList : new ArrayList<>());
        advertisement.setMatchedUserId(this.matchedUserId);
        return advertisement;
    }
}
