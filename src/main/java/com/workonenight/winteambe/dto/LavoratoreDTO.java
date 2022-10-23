package com.workonenight.winteambe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LavoratoreDTO extends BaseUserDTO{

    private String firstName;
    private String lastName;

    //skill id matched with skill DTO
    private List<String> skillIds;
    private List<SkillDTO> skillList;
    private List<String> availabilityDays;
    private List<String> availabilityHourSlots;
    private List<String> availabilityCities;
    private boolean verified;
    private List<String> influencedUserList;


    public static LavoratoreDTO fromUserDTOtoLavoratoreDTO(UserDTO userDTO){
        LavoratoreDTO lavoratoreDTO = new LavoratoreDTO();
        lavoratoreDTO.setId(userDTO.getId());
        lavoratoreDTO.setFirstName(userDTO.getFirstName());
        lavoratoreDTO.setLastName(userDTO.getLastName());
        lavoratoreDTO.setEmail(userDTO.getEmail());
        lavoratoreDTO.setRoleId(userDTO.getRoleId());
        lavoratoreDTO.setDescription(userDTO.getDescription());
        lavoratoreDTO.setBrief(userDTO.getBrief());
        lavoratoreDTO.setSkillIds(userDTO.getSkillIds());
        lavoratoreDTO.setSkillList(userDTO.getSkillList());
        lavoratoreDTO.setAvailabilityDays(userDTO.getAvailabilityDays());
        lavoratoreDTO.setAvailabilityHourSlots(userDTO.getAvailabilityHourSlots());
        lavoratoreDTO.setAvailabilityCities(userDTO.getAvailabilityCities());
        lavoratoreDTO.setAddress(userDTO.getAddress());
        lavoratoreDTO.setCity(userDTO.getCity());
        lavoratoreDTO.setProvince(userDTO.getProvince());
        lavoratoreDTO.setNation(userDTO.getNation());
        lavoratoreDTO.setPhoneNumber(userDTO.getPhoneNumber());
        lavoratoreDTO.setImageLink(userDTO.getImageLink());
        lavoratoreDTO.setVerified(userDTO.isVerified());
        lavoratoreDTO.setInfluencedUserList(userDTO.getInfluencedUserList());
        return lavoratoreDTO;
    }
}
