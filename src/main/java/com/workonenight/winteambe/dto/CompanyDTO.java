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

    private int contatoreAnnunci;
    //subscription id to join in the subscription collection
    private String subscriptionId;
    private LocalDateTime lastSubscriptionDate;
    //company type id to join in the company type collection
    private List<String> companyTypeId;
    private boolean verified;
    private boolean enabledAnnunci;

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
        companyDTO.setContatoreAnnunci(userDTO.getContatoreAnnunci());
        companyDTO.setSubscriptionId(userDTO.getSubscriptionId());
        companyDTO.setLastSubscriptionDate(userDTO.getLastSubscriptionDate());
        companyDTO.setCompanyTypeId(userDTO.getCompanyTypeId());
        companyDTO.setVerified(userDTO.isVerified());
        companyDTO.setEnabledAnnunci(userDTO.isEnabledAnnunci());
        return companyDTO;
    }
}
