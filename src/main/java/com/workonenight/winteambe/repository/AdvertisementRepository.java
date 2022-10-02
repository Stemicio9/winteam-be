package com.workonenight.winteambe.repository;

import com.workonenight.winteambe.entity.Advertisement;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdvertisementRepository extends MongoRepository<Advertisement, String> {
}
