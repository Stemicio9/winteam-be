package com.workonenight.winteambe.dto;

import com.workonenight.winteambe.entity.Advertisement;
import com.workonenight.winteambe.entity.interfaces.DataTransferObject;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class AdvertisementDTO extends DataTransferObject {
    private String id;
    private String description;
    private LocalDateTime date;
    private String hourSlot;

    //skill id matched with skill DTO

    private SkillDTO skill = new SkillDTO();

    private Double payment;

    private String position;
    private BaseUserDTO publisherUser = new BaseUserDTO();
    private List<BaseUserDTO> candidateUserList;

    private BaseUserDTO matchedUser = new BaseUserDTO();
    private String advertisementStatus;

    //convert dto to entity
    public Advertisement toEntity() {
        Advertisement advertisement = new Advertisement();
        advertisement.setId(this.id);
        advertisement.setDescription(this.description);
        advertisement.setDate(this.date);
        advertisement.setHourSlot(this.hourSlot);
        advertisement.setSkill(this.skill.toEntity());
        advertisement.setPayment(this.payment);
        advertisement.setPosition(this.position);
        advertisement.setPublisherUser(this.publisherUser.toEntity());
        advertisement.setCandidateUserList((this.candidateUserList != null) ? this.candidateUserList.stream().map(BaseUserDTO::toEntity).collect(Collectors.toList()) : new ArrayList<>());
        advertisement.setMatchedUser((this.matchedUser != null) ? this.matchedUser.toEntity() : null);
        return advertisement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdvertisementDTO that = (AdvertisementDTO) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public DataTransferObject createBaseDTO(){
        return new AdvertisementDTO();
    }
}
