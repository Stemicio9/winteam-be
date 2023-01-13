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


    //subscription
    private String subscriptionName;
    private String subscriptionImageLink;
    private int advertisementLeft;
    private LocalDateTime expiringSubscriptionDate;
    private boolean searchEnabled;
    private boolean createAdvertisementEnabled;


    //company type id to join in the company type collection
    private List<String> companyTypeId;
    private boolean verified;
    private double rating;
    private List<String> influencedUserList;

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

        user.setSubscriptionName(this.subscriptionName);
        user.setSubscriptionImageLink(this.subscriptionImageLink);
        user.setAdvertisementLeft(this.advertisementLeft);
        user.setExpiringSubscriptionDate(this.expiringSubscriptionDate);
        user.setSearchEnabled(this.searchEnabled);
        user.setCreateAdvertisementEnabled(this.createAdvertisementEnabled);


        user.setCompanyTypeId(this.companyTypeId);
        user.setVerified(this.verified);
        user.setRating(this.rating);
        user.setInfluencedUserList(this.influencedUserList);
        return user;
    }
}
