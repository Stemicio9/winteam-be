package com.workonenight.winteambe.dto;

import com.workonenight.winteambe.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO extends BaseUserDTO {
    private String firstName;
    private String lastName;
    private String companyName;

    //skill id matched with skill DTO
    private List<String> skillIds;
    private List<SkillDTO> skillList;
    private List<String> availabilityDays;
    private List<String> availabilityHourSlots;
    private List<String> availabilityCities;
    private int contatoreAnnunci;
    //subscription id to join in the subscription collection
    private String subscriptionId;
    private LocalDateTime lastSubscriptionDate;
    //company type id to join in the company type collection
    private List<String> companyTypeId;
    private boolean verified;
    private List<String> influencedUserList;

    private boolean enabledAnnunci;

    public User toEntity(){
        User user = new User();
        user.setId(this.getId());
        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);
        user.setCompanyName(this.companyName);
        user.setEmail(this.getEmail());
        user.setRoleId(this.getRoleId());
        user.setDescription(this.getDescription());
        user.setBrief(this.getBrief());
        user.setSkillList(this.skillIds != null ? this.skillIds : new ArrayList<>());
        user.setAvailabilityDays(this.availabilityDays != null ? this.availabilityDays : new ArrayList<>());
        user.setAvailabilityHourSlots(this.availabilityHourSlots != null ? this.availabilityHourSlots : new ArrayList<>());
        user.setAvailabilityCities(this.availabilityCities != null ? this.availabilityCities : new ArrayList<>());
        user.setAddress(this.getAddress());
        user.setCity(this.getCity());
        user.setProvince(this.getProvince());
        user.setNation(this.getNation());
        user.setPhoneNumber(this.getPhoneNumber());
        user.setImageLink(this.getImageLink());
        user.setContatoreAnnunci(this.contatoreAnnunci);
        user.setSubscriptionId(this.subscriptionId);
        user.setLastSubscriptionDate(this.lastSubscriptionDate);
        user.setCompanyTypeId(this.companyTypeId);
        user.setVerified(this.verified);
        user.setInfluencedUserList(this.influencedUserList);
        return user;
    }
}
