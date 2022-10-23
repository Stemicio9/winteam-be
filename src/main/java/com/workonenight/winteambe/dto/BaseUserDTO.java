package com.workonenight.winteambe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseUserDTO {

    private String id;
    private String email;
    private String roleId;
    private String description;
    private String brief;
    private String address;
    private String city;
    private String province;
    private String nation;
    private String phoneNumber;
    private String imageLink;
}
