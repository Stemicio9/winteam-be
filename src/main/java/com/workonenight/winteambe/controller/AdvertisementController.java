package com.workonenight.winteambe.controller;

import com.workonenight.winteambe.dto.AdvertisementDTO;
import com.workonenight.winteambe.service.AdvertisementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/advertisement")
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    public AdvertisementController(AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;
    }

    @GetMapping(value = "/list/all")
    public List<AdvertisementDTO> getAllAdvertisements() {
        return advertisementService.getAllAdvertisements();
    }

    @GetMapping(value = "/list/{id}")
    public AdvertisementDTO getAdvertisementById(@PathVariable("id") String id) {
        return advertisementService.getAdvertisementById(id);
    }

    @PostMapping(value = "/create")
    public AdvertisementDTO createAdvertisement(@RequestBody AdvertisementDTO advertisementDTO) {
        return advertisementService.createAdvertisement(advertisementDTO);
    }

    @PostMapping(value = "/update")
    public AdvertisementDTO updateAdvertisement(@RequestBody AdvertisementDTO advertisementDTO) {
        return advertisementService.updateAdvertisement(advertisementDTO);
    }

}
