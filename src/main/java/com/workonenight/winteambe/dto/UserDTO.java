package com.workonenight.winteambe.dto;

import com.workonenight.winteambe.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String companyName;
    private String email;
    //role id to join in the role collection
    private String roleId;
    private String description;
    private String brief;
    private List<String> skillIds;

    private List<SkillDTO> skillList;
    private List<String> availabilityDays;
    private List<String> availabilityHourSlots;
    private List<String> availabilityCities;
    private String address;
    private String city;
    private String province;
    private String nation;
    private String phoneNumber;
    private String imageLink;
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
        user.setId(this.id);
        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);
        user.setCompanyName(this.companyName);
        user.setEmail(this.email);
        user.setRoleId(this.roleId);
        user.setDescription(this.description);
        user.setBrief(this.brief);
        user.setSkillList(this.skillIds);
        user.setAvailabilityDays(this.availabilityDays);
        user.setAvailabilityHourSlots(this.availabilityHourSlots);
        user.setAvailabilityCities(this.availabilityCities);
        user.setAddress(this.address);
        user.setCity(this.city);
        user.setProvince(this.province);
        user.setNation(this.nation);
        user.setPhoneNumber(this.phoneNumber);
        user.setImageLink(this.imageLink);
        user.setContatoreAnnunci(this.contatoreAnnunci);
        user.setSubscriptionId(this.subscriptionId);
        user.setLastSubscriptionDate(this.lastSubscriptionDate);
        user.setCompanyTypeId(this.companyTypeId);
        user.setVerified(this.verified);
        user.setInfluencedUserList(this.influencedUserList);
        return user;
    }
}
