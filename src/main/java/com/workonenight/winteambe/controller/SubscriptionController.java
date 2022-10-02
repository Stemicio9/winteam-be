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

    /**
     * Get all subscriptions
     * @return List<SubscriptionDTO> List of subscriptions
     */
    @GetMapping(value = "/list/all")
    public List<SubscriptionDTO> getAllSubscriptions() {
        return subscriptionService.getAllSubscriptions();
    }

    /**
     * Get subscription by id
     * @param id Subscription id
     * @return SubscriptionDTO Subscription
     */
    @GetMapping(value = "/list/{id}")
    public SubscriptionDTO getSubscriptionById(@PathVariable("id") String id) {
        return subscriptionService.getSubscriptionById(id);
    }

    /**
     * Create subscription
     * @param subscriptionDTO SubscriptionDTO
     * @return SubscriptionDTO Subscription
     */
    @PostMapping(value = "/create")
    public SubscriptionDTO createSubscription(@RequestBody SubscriptionDTO subscriptionDTO) {
        return subscriptionService.createSubscription(subscriptionDTO);
    }

    /**
     * Update subscription
     * @param subscriptionDTO SubscriptionDTO
     * @return SubscriptionDTO Subscription
     */
    @PostMapping(value = "/update")
    public SubscriptionDTO updateSubscription(@RequestBody SubscriptionDTO subscriptionDTO) {
        return subscriptionService.updateSubscription(subscriptionDTO);
    }
}
