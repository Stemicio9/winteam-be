package com.workonenight.winteambe.dto;

import com.workonenight.winteambe.entity.Advertisement;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AdvertisementRequest {

    private String id;
    private String title;
    private String description;
    private LocalDateTime date;
    private String hourSlot;
    private String skill;
    private Double payment;
    private String publisherUserId;
    private List<String> candidateUserList;
    private String matchedUserId;
    private String advertisementStatus;

    public Advertisement toEntity() {
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
        return advertisement;
    }



}
