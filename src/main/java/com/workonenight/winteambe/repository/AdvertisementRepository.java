package com.workonenight.winteambe.repository;

import com.workonenight.winteambe.common.ResourceRepository;
import com.workonenight.winteambe.entity.Advertisement;

import java.util.List;

public interface AdvertisementRepository extends ResourceRepository<Advertisement, String> {

    List<Advertisement> findAllByPublisherUserId(String publisherUserId);
}
