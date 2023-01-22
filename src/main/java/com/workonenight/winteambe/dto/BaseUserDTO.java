package com.workonenight.winteambe.dto;

import com.workonenight.winteambe.entity.User;
import com.workonenight.winteambe.entity.annotations.anonymous.Anonymous;
import com.workonenight.winteambe.entity.interfaces.Anonymizable;
import com.workonenight.winteambe.entity.interfaces.DataTransferObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseUserDTO extends DataTransferObject implements Anonymizable {

    private String id;
    private String firstName;
    @Anonymous
    private String lastName;
    private String companyName;
    @Anonymous
    private String email;
    private String roleId;
    private String description;
    @Anonymous
    private String brief;
    private String address;
    private String city;
    private String province;
    private String nation;

    @Anonymous
    private String phoneNumber;
    @Anonymous
    private String imageLink;

    private List<SkillDTO> skillList;


    //subscription
    private String subscriptionName;
    private String subscriptionImageLink;
    private int advertisementLeft;
    private LocalDateTime expiringSubscriptionDate;
    private boolean searchEnabled;
    private boolean createAdvertisementEnabled;

    //company type id to join in the company type collection


    private double rating;



    @Override
    public DataTransferObject createBaseDTO(){
        return new BaseUserDTO();
    }


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
        user.setSkillList((this.skillList != null) ? this.skillList.stream().map(SkillDTO::toEntity).collect(Collectors.toList()) : new ArrayList<>());
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
        user.setRating(this.rating);
        return user;
    }

    @Override
    public boolean shouldBeAnonymized() {
        return !StringUtils.hasLength(subscriptionName);
    }


}
