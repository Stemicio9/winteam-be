package com.workonenight.winteambe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchAdvertisementRequest {

    private String userId;
    private String advertisementId;
}
