package com.workonenight.winteambe.entity;

import com.workonenight.winteambe.dto.AdvertisementDTO;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Data
@Document(collection = "advertisements")
public class Advertisement implements Serializable {

    @Id
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

    //convert entity to DTO
    public AdvertisementDTO toDTO(){
        AdvertisementDTO advertisementDTO = new AdvertisementDTO();
        advertisementDTO.setId(this.id);
        advertisementDTO.setTitle(this.title);
        advertisementDTO.setDescription(this.description);
        advertisementDTO.setDate(this.date);
        advertisementDTO.setHourSlot(this.hourSlot);
        advertisementDTO.setSkill(this.skill);
        advertisementDTO.setPayment(this.payment);
        advertisementDTO.setPublisherUserId(this.publisherUserId);
        advertisementDTO.setCandidateUserList(this.candidateUserList);
        advertisementDTO.setMatchedUserId(this.matchedUserId);
        // TODO calculate advertisement status
        //advertisementDTO.setAdvertisementStatus(this.advertisementStatus);
        return advertisementDTO;
    }

    public Advertisement toUpdateEntity(AdvertisementDTO advertisementDTO) {
        this.title = advertisementDTO.getTitle();
        this.description = advertisementDTO.getDescription();
        this.date = advertisementDTO.getDate();
        this.hourSlot = advertisementDTO.getHourSlot();
        this.skill = advertisementDTO.getSkill();
        this.payment = advertisementDTO.getPayment();
        this.publisherUserId = advertisementDTO.getPublisherUserId();
        this.candidateUserList = advertisementDTO.getCandidateUserList();
        this.matchedUserId = advertisementDTO.getMatchedUserId();
        // TODO calculate advertisement status
        //this.advertisementStatus = advertisementDTO.getAdvertisementStatus();
        return this;
    }
}
