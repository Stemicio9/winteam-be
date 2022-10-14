package com.workonenight.winteambe.service;

import com.google.firebase.auth.FirebaseToken;
import com.workonenight.winteambe.dto.AdvertisementDTO;
import com.workonenight.winteambe.entity.Advertisement;
import com.workonenight.winteambe.repository.AdvertisementRepository;
import com.workonenight.winteambe.service.other.FirebaseService;
import com.workonenight.winteambe.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AdvertisementService {

    private final FirebaseService firebaseService;
    private final AdvertisementRepository advertisementRepository;

    public AdvertisementService(AdvertisementRepository advertisementRepository, FirebaseService firebaseService) {
        this.advertisementRepository = advertisementRepository;
        this.firebaseService = firebaseService;
    }

    public List<AdvertisementDTO> getAllAdvertisements() {
        return advertisementRepository.findAll().stream().map(Advertisement::toDTO).collect(Collectors.toList());
    }

    public AdvertisementDTO getAdvertisementById(String id) {
        return advertisementRepository.findById(id).orElseThrow().toDTO();
    }

    public AdvertisementDTO createAdvertisement(AdvertisementDTO advertisementDTO) {
        Advertisement advertisement = advertisementDTO.toEntity();
        return advertisementRepository.save(advertisement).toDTO();
    }

    public AdvertisementDTO updateAdvertisement(AdvertisementDTO advertisementDTO) {
        Advertisement advertisement = advertisementRepository.findById(advertisementDTO.getId()).orElse(null);
        if (advertisement != null) {
            advertisement = advertisement.toUpdateEntity(advertisementDTO);
            return advertisementRepository.save(advertisement).toDTO();
        }
        return null;
    }

    public List<AdvertisementDTO> getAdvertisementByOwnerAndState(HttpServletRequest request, String state) {
        log.info("Entering method getAdvertisementByOwnerAndState");
        FirebaseToken firebaseToken = firebaseService.getFirebaseToken(request);
        if (firebaseToken != null) {
            String userId = firebaseToken.getUid();
            List<Advertisement> advertisements = advertisementRepository.findAllByPublisherUserId(userId);
            log.info("Found {} advertisements", advertisements.size());
            if (!Objects.equals(state, Utils.AdvertisementState.ALL)) {
                return advertisements.stream().map(Advertisement::toDTO).filter(advertisementDTO -> advertisementDTO.getAdvertisementStatus().equals(state)).collect(Collectors.toList());
            } else {
                return advertisements.stream().map(Advertisement::toDTO).collect(Collectors.toList());
            }
        } else {
            log.error("Firebase token is null");
        }
        log.info("No advertisements found");
        return List.of();
    }

    public Page<AdvertisementDTO> getPageFiltered(Query query, Pageable pageable) {
        log.info("Entering method getPageFiltered");
        log.info("Query: {}", query);
        Page<AdvertisementDTO> page = advertisementRepository.findAll(query, pageable).map(Advertisement::toDTO);
        log.info("Found {} advertisements", page.getTotalElements());
        return page;
    }

    public List<AdvertisementDTO> getAllFiltered(Query query) {
        return advertisementRepository.findAll(query).stream().map(Advertisement::toDTO).collect(Collectors.toList());
    }
}
