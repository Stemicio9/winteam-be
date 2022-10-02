package com.workonenight.winteambe.entity;

import com.workonenight.winteambe.dto.UserDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "users")
public class User implements Serializable {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    //role id to join in the role collection
    private String roleId;
    private String description;
    private String brief;
    private List<String> skillList;
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

    //TODO enable this when the subscription is ready
    // private boolean enabledAnnunci;
    private List<String> influencedUserList;

    public User(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public UserDTO toDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(this.id);
        userDTO.setFirstName(this.firstName);
        userDTO.setLastName(this.lastName);
        userDTO.setEmail(this.email);
        userDTO.setRoleId(this.roleId);
        userDTO.setDescription(this.description);
        userDTO.setBrief(this.brief);
        userDTO.setSkillList(this.skillList);
        userDTO.setAvailabilityDays(this.availabilityDays);
        userDTO.setAvailabilityHourSlots(this.availabilityHourSlots);
        userDTO.setAvailabilityCities(this.availabilityCities);
        userDTO.setAddress(this.address);
        userDTO.setCity(this.city);
        userDTO.setProvince(this.province);
        userDTO.setNation(this.nation);
        userDTO.setPhoneNumber(this.phoneNumber);
        userDTO.setImageLink(this.imageLink);
        userDTO.setContatoreAnnunci(this.contatoreAnnunci);
        userDTO.setSubscriptionId(this.subscriptionId);
        userDTO.setLastSubscriptionDate(this.lastSubscriptionDate);
        userDTO.setCompanyTypeId(this.companyTypeId);
        userDTO.setVerified(this.verified);
        userDTO.setInfluencedUserList(this.influencedUserList);
        //TODO: add enabledAnnunci
        //userDTO.setEnabledAnnunci(Utils.calculateEnabledAnnunci(this.contatoreAnnunci, this.subscriptionId));
        return userDTO;
    }

    public User toUpdateEntity(UserDTO userDTO) {
        this.firstName = userDTO.getFirstName();
        this.lastName = userDTO.getLastName();
        this.email = userDTO.getEmail();
        this.roleId = userDTO.getRoleId();
        this.description = userDTO.getDescription();
        this.brief = userDTO.getBrief();
        this.skillList = userDTO.getSkillList();
        this.availabilityDays = userDTO.getAvailabilityDays();
        this.availabilityHourSlots = userDTO.getAvailabilityHourSlots();
        this.availabilityCities = userDTO.getAvailabilityCities();
        this.address = userDTO.getAddress();
        this.city = userDTO.getCity();
        this.province = userDTO.getProvince();
        this.nation = userDTO.getNation();
        this.phoneNumber = userDTO.getPhoneNumber();
        this.imageLink = userDTO.getImageLink();
        this.contatoreAnnunci = userDTO.getContatoreAnnunci();
        this.subscriptionId = userDTO.getSubscriptionId();
        this.lastSubscriptionDate = userDTO.getLastSubscriptionDate();
        this.companyTypeId = userDTO.getCompanyTypeId();
        this.verified = userDTO.isVerified();
        this.influencedUserList = userDTO.getInfluencedUserList();
        //TODO: add enabledAnnunci
        //this.enabledAnnunci = Utils.calculateEnabledAnnunci(userDTO.getContatoreAnnunci(), userDTO.getSubscriptionId());
        return this;
    }


}
