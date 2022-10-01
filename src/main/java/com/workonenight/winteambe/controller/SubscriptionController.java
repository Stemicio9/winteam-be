package com.workonenight.winteambe.controller;

import com.workonenight.winteambe.dto.SubscriptionDTO;
import com.workonenight.winteambe.service.SubscriptionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/subscription")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping(value = "/list/all")
    public List<SubscriptionDTO> getAllSubscriptions() {
        return subscriptionService.getAllSubscriptions();
    }

    @GetMapping(value = "/list/{id}")
    public SubscriptionDTO getSubscriptionById(@PathVariable("id") String id) {
        return subscriptionService.getSubscriptionById(id);
    }

    @PostMapping(value = "/create")
    public SubscriptionDTO createSubscription(@RequestBody SubscriptionDTO subscriptionDTO) {
        return subscriptionService.createSubscription(subscriptionDTO);
    }

    @PostMapping(value = "/update")
    public SubscriptionDTO updateSubscription(@RequestBody SubscriptionDTO subscriptionDTO) {
        return subscriptionService.updateSubscription(subscriptionDTO);
    }
}
