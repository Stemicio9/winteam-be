package com.workonenight.winteambe.entity;

import com.workonenight.winteambe.dto.BaseUserDTO;
import com.workonenight.winteambe.entity.interfaces.DataTransferObject;
import com.workonenight.winteambe.entity.interfaces.Transferrable;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@Document(collection = "users")
public class User extends DataTransferObject implements Serializable, Transferrable {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String companyName;
    private String email;
    //role id to join in the role collection
    private String roleId;
    private String description;
    private String brief;

    @DBRef
    private List<Skill> skillList = new ArrayList<>();
    private List<String> availabilityDays;
    private List<String> availabilityHourSlots;
    private List<String> availabilityCities;
    private String address;
    private String city;
    private String province;
    private String nation;
    private String phoneNumber;
    private String imageLink;


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

    public User(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public boolean shouldBeAnonymized() {
        return StringUtils.hasLength(this.getSubscriptionName());
    }
    @Override
    public DataTransferObject asDTO() {
        return new BaseUserDTO();
    }

    @Override
    public DataTransferObject createBaseDTO(){
        return new BaseUserDTO();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
