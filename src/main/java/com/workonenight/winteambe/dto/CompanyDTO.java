package com.workonenight.winteambe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO extends BaseUserDTO{

    private String companyName;

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

    public static CompanyDTO fromUserDTOtoCompanyDTO(UserDTO userDTO){
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setId(userDTO.getId());
        companyDTO.setCompanyName(userDTO.getCompanyName());
        companyDTO.setEmail(userDTO.getEmail());
        companyDTO.setRoleId(userDTO.getRoleId());
        companyDTO.setDescription(userDTO.getDescription());
        companyDTO.setBrief(userDTO.getBrief());
        companyDTO.setAddress(userDTO.getAddress());
        companyDTO.setCity(userDTO.getCity());
        companyDTO.setProvince(userDTO.getProvince());
        companyDTO.setNation(userDTO.getNation());
        companyDTO.setPhoneNumber(userDTO.getPhoneNumber());
        companyDTO.setImageLink(userDTO.getImageLink());

        companyDTO.setSubscriptionName(userDTO.getSubscriptionName());
        companyDTO.setSubscriptionImageLink(userDTO.getSubscriptionImageLink());
        companyDTO.setAdvertisementLeft(userDTO.getAdvertisementLeft());
        companyDTO.setExpiringSubscriptionDate(userDTO.getExpiringSubscriptionDate());
        companyDTO.setSearchEnabled(userDTO.isSearchEnabled());
        companyDTO.setCreateAdvertisementEnabled(userDTO.isCreateAdvertisementEnabled());


        companyDTO.setCompanyTypeId(userDTO.getCompanyTypeId());
        companyDTO.setVerified(userDTO.isVerified());
        companyDTO.setRating(userDTO.getRating());
        return companyDTO;
    }
}
