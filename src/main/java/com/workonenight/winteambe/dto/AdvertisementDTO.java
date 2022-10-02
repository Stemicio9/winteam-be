package com.workonenight.winteambe.dto;

import com.workonenight.winteambe.entity.Advertisement;
import lombok.Data;

import java.util.List;

@Data
public class AdvertisementDTO {
    private String id;
    private String title;
    private String description;
    private String date;
    private String hourSlot;
    private String skill;
    private String payment;
    private String publisherUserId;
    private List<String> candidateUserList;
    private String matchedUserId;
    private String advertisementStatus;

    //convert dto to entity
    public Advertisement toEntity(){
        Advertisement advertisement = new Advertisement();
        advertisement.setId(this.id);
        advertisement.setTitle(this.title);
        advertisement.setDescription(this.description);
        advertisement.setDate(this.date);
        advertisement.setHourSlot(this.hourSlot);
        advertisement.setSkill(this.skill);
        advertisement.setPayment(this.payment);
        advertisement.setPublisherUserId(this.publisherUserId);
        advertisement.setCandidateUserList(this.candidateUserList);
        advertisement.setMatchedUserId(this.matchedUserId);
        advertisement.setAdvertisementStatus(this.advertisementStatus);
        return advertisement;
    }
}
