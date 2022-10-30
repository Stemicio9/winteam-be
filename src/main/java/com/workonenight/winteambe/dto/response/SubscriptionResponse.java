package com.workonenight.winteambe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionResponse {
    private String subscriptionName;
    private String subscriptionImageLink;
    private int advertisementLeft;
    private LocalDateTime expiringSubscriptionDate;
    private boolean searchEnabled;
    private boolean createAdvertisementEnabled;
}
