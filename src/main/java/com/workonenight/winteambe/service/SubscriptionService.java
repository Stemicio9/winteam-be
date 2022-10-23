package com.workonenight.winteambe.service;

import com.workonenight.winteambe.dto.SubscriptionDTO;
import com.workonenight.winteambe.entity.Subscription;
import com.workonenight.winteambe.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public List<SubscriptionDTO> getAllSubscriptions() {
        return subscriptionRepository.findAll().stream().map(Subscription::toDTO).collect(Collectors.toList());
    }

    public SubscriptionDTO getSubscriptionById(String id) {
        Subscription subscription = subscriptionRepository.findById(id).orElse(null);
        return subscription != null ? subscription.toDTO() : null;
    }

    public SubscriptionDTO createSubscription(SubscriptionDTO subscriptionDTO) {
        Subscription subscription = subscriptionDTO.toEntity();
        return subscriptionRepository.save(subscription).toDTO();
    }

    public SubscriptionDTO updateSubscription(SubscriptionDTO subscriptionDTO) {
        Subscription subscription = subscriptionRepository.findById(subscriptionDTO.getId()).orElse(null);
        if (subscription != null) {
            subscription = subscription.toUpdateEntity(subscriptionDTO);
            return subscriptionRepository.save(subscription).toDTO();
        }
        return null;
    }

    public boolean existsById(String id) {
        return subscriptionRepository.existsById(id);
    }
}
