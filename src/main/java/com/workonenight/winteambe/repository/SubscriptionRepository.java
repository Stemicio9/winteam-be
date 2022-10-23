package com.workonenight.winteambe.repository;

import com.workonenight.winteambe.entity.Subscription;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubscriptionRepository extends MongoRepository<Subscription, String> {

    boolean existsById(String id);
}
