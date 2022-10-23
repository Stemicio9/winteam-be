package com.workonenight.winteambe.service;

import com.google.firebase.auth.FirebaseToken;
import com.workonenight.winteambe.dto.AdvertisementDTO;
import com.workonenight.winteambe.dto.BaseUserDTO;
import com.workonenight.winteambe.dto.UserDTO;
import com.workonenight.winteambe.entity.Advertisement;
import com.workonenight.winteambe.entity.User;
import com.workonenight.winteambe.repository.AdvertisementRepository;
import com.workonenight.winteambe.service.other.FirebaseService;
import com.workonenight.winteambe.utils.UserType;
import com.workonenight.winteambe.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AdvertisementService {

    private final FirebaseService firebaseService;
    private final UserService userService;
    private final SkillService skillService;
    private final AdvertisementRepository advertisementRepository;

    public AdvertisementService(AdvertisementRepository advertisementRepository, FirebaseService firebaseService, UserService userService, SkillService skillService) {
        this.advertisementRepository = advertisementRepository;
        this.firebaseService = firebaseService;
        this.userService = userService;
        this.skillService = skillService;
    }

    public List<AdvertisementDTO> getAllAdvertisements() {
        return advertisementRepository.findAll().stream().map(Advertisement::toDTO).peek(this::finalizeAdvertisementDTO).collect(Collectors.toList());
    }

    public AdvertisementDTO getAdvertisementById(String id) {
        log.info("Searching advertisement for id: {}", id);
        Optional<Advertisement> opt = advertisementRepository.findById(id);
        if (opt.isPresent()) {
            AdvertisementDTO advertisementDTO = opt.get().toDTO();
            return finalizeAdvertisementDTO(advertisementDTO);
        }
        return null;
    }

    public AdvertisementDTO createAdvertisement(HttpServletRequest request, AdvertisementDTO advertisementDTO) {
        log.info("Creating advertisement: '{}'", advertisementDTO.getTitle());
        UserDTO userDTO = (UserDTO) userService.getMe(request);
        if (userDTO != null) {
            Advertisement advertisement = advertisementDTO.toEntity();
            advertisement.setId(null);
            Utils.checkHourSlot(advertisement.getHourSlot());
            advertisement.setPublisherUserId(userDTO.getId());
            advertisement.setCandidateUserList(new ArrayList<>());

            //saving and returning advertisementDTO with skillDTO and publisherUserDTO filled
            AdvertisementDTO result = advertisementRepository.save(advertisement).toDTO();
            return this.finalizeAdvertisementDTO(result);
        } else {
            log.error("User not found, can't create advertisement");
            return null;
        }
    }

    //TODO fix this method
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
            if (!Objects.equals(state, Utils.AdvertisementDatoreState.ALL)) {
                return advertisements.stream().map(Advertisement::toDTO).filter(advertisementDTO -> advertisementDTO.getAdvertisementStatus().equals(state)).peek(this::finalizeAdvertisementDTO).collect(Collectors.toList());
            } else {
                return advertisements.stream().map(Advertisement::toDTO).peek(this::finalizeAdvertisementDTO).collect(Collectors.toList());
            }
        } else {
            log.error("Firebase token is null");
        }
        log.info("No advertisements found");
        return List.of();
    }

    public Page<AdvertisementDTO> getPageFiltered(HttpServletRequest request, String state, Query query, Pageable pageable) {
        log.info("Entering method getPageFiltered");
        log.info("Query: {}", query);
        BaseUserDTO user = userService.getUserById(firebaseService.getFirebaseToken(request).getUid());
        String userRole = user.getRoleId();
        switch (userRole) {
            case UserType.DATORE:
                return advertisementRepository.findAll(query, pageable).map(Advertisement::toDTO).map(this::finalizeAdvertisementDTO);
            case UserType.LAVORATORE:
                String userId = user.getId();
                if (Objects.equals(state, Utils.AdvertisementDatoreState.ALL)) {
                    return advertisementRepository.findAll(query, pageable).map(a -> a.toDTOLavoratore(userId)).map(this::finalizeAdvertisementDTO);
                } else {
                    Page<AdvertisementDTO> page = advertisementRepository.findAll(query, pageable).map(a -> a.toDTOLavoratore(userId)).map(this::finalizeAdvertisementDTO);
                    List<AdvertisementDTO> content = page.getContent();
                    return new PageImpl<>(content.stream().filter(advertisementDTO -> advertisementDTO.getAdvertisementStatus().equals(state)).collect(Collectors.toList()), pageable, page.getTotalElements());
                }
            default:
                log.error("User role not valid");
                return null;
        }
    }

    public List<AdvertisementDTO> getAllFiltered(Query query) {
        log.info("Get all advertisements filtered");
        return advertisementRepository.findAll(query).stream().map(Advertisement::toDTO).peek(this::finalizeAdvertisementDTO).collect(Collectors.toList());
    }

    public List<BaseUserDTO> getAllUsersRelated(String id) {
        Advertisement advertisement = advertisementRepository.findById(id).orElse(null);
        if (advertisement != null) {
            List<String> userIdList = advertisement.getCandidateUserList();
            List<BaseUserDTO> result = new ArrayList<>();
            for (String userId : userIdList) {
                result.add(userService.getUserById(userId));
            }
            return result;
        }
        return new ArrayList<>();
    }


    private AdvertisementDTO finalizeAdvertisementDTO(AdvertisementDTO advertisementDTO) {
        if (advertisementDTO.getSkillId() != null) {
            advertisementDTO.setSkillDTO(skillService.getSkillById(advertisementDTO.getSkillId()));
        } else {
            advertisementDTO.setSkillDTO(null);
        }

        if (advertisementDTO.getPublisherUserId() != null) {
            advertisementDTO.setPublisherUserDTO(userService.getUserById(advertisementDTO.getPublisherUserId()));
        } else {
            advertisementDTO.setPublisherUserDTO(null);
        }
        return advertisementDTO;
    }
}
