package com.workonenight.winteambe.entity;

import com.workonenight.winteambe.dto.AdvertisementDTO;
import com.workonenight.winteambe.utils.Utils;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "advertisements")
public class Advertisement implements Serializable {

    @Id
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

    //convert entity to DTO
    public AdvertisementDTO toDTO() {
        AdvertisementDTO advertisementDTO = new AdvertisementDTO();
        advertisementDTO.setId(this.id);
        advertisementDTO.setTitle(this.title);
        advertisementDTO.setDescription(this.description);
        advertisementDTO.setDate(this.date.format(Utils.DATE_TIME_FORMATTER));
        advertisementDTO.setHourSlot(this.hourSlot);
        advertisementDTO.setSkill(this.skill);
        advertisementDTO.setPayment(this.payment);
        advertisementDTO.setPublisherUserId(this.publisherUserId);
        advertisementDTO.setCandidateUserList(this.candidateUserList.size() == 0 ? new ArrayList<>() : this.candidateUserList);
        advertisementDTO.setMatchedUserId(this.matchedUserId);
        advertisementDTO.setAdvertisementStatus(Utils.calculateAdvertisementStatus(this.date, this.matchedUserId));
        return advertisementDTO;
    }

    public Advertisement toUpdateEntity(AdvertisementDTO advertisementDTO) {
        this.title = advertisementDTO.getTitle();
        this.description = advertisementDTO.getDescription();
        this.date = LocalDateTime.parse(advertisementDTO.getDate());
        this.hourSlot = advertisementDTO.getHourSlot();
        this.skill = advertisementDTO.getSkill();
        this.payment = advertisementDTO.getPayment();
        this.publisherUserId = advertisementDTO.getPublisherUserId();
        this.candidateUserList = advertisementDTO.getCandidateUserList().size() == 0 ? new ArrayList<>() : advertisementDTO.getCandidateUserList();
        this.matchedUserId = advertisementDTO.getMatchedUserId();
        return this;
    }
}
