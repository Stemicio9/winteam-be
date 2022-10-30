package com.workonenight.winteambe.dto.request;

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
