package com.workonenight.winteambe.controller;

import com.workonenight.winteambe.service.SubscriptionService;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/subscription")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }
}
