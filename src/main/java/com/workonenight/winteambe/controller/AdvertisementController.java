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

    /**
     * Get all advertisements
     * @return List<AdvertisementDTO> List of advertisements
     */
    @GetMapping(value = "/list/all")
    public List<AdvertisementDTO> getAllAdvertisements() {
        return advertisementService.getAllAdvertisements();
    }

    /**
     * Get advertisement by id
     * @param id Advertisement id
     * @return AdvertisementDTO Advertisement
     */
    @GetMapping(value = "/list/{id}")
    public AdvertisementDTO getAdvertisementById(@PathVariable("id") String id) {
        return advertisementService.getAdvertisementById(id);
    }

    /**
     * Create advertisement
     * @param advertisementDTO AdvertisementDTO
     * @return AdvertisementDTO Advertisement
     */
    @PostMapping(value = "/create")
    public AdvertisementDTO createAdvertisement(@RequestBody AdvertisementDTO advertisementDTO) {
        return advertisementService.createAdvertisement(advertisementDTO);
    }

    /**
     * Update advertisement
     * @param advertisementDTO AdvertisementDTO
     * @return AdvertisementDTO Advertisement
     */
    @PostMapping(value = "/update")
    public AdvertisementDTO updateAdvertisement(@RequestBody AdvertisementDTO advertisementDTO) {
        return advertisementService.updateAdvertisement(advertisementDTO);
    }

}
