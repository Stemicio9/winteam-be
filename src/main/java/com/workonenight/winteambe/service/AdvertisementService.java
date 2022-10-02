package com.workonenight.winteambe.service;

import com.workonenight.winteambe.dto.AdvertisementDTO;
import com.workonenight.winteambe.entity.Advertisement;
import com.workonenight.winteambe.repository.AdvertisementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;

    public AdvertisementService(AdvertisementRepository advertisementRepository) {
        this.advertisementRepository = advertisementRepository;
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
}
