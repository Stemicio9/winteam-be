package com.workonenight.winteambe.service;

import com.google.firebase.auth.FirebaseToken;
import com.workonenight.winteambe.dto.AdvertisementDTO;
import com.workonenight.winteambe.dto.BaseUserDTO;
import com.workonenight.winteambe.entity.Advertisement;
import com.workonenight.winteambe.repository.AdvertisementRepository;
import com.workonenight.winteambe.service.other.FirebaseService;
import com.workonenight.winteambe.utils.UserType;
import com.workonenight.winteambe.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
        log.info("Creating advertisement");
        BaseUserDTO userDTO = userService.getMe(request);
        if (userDTO != null) {
            Advertisement advertisement = advertisementDTO.toEntity();
            advertisement.setId(null);
            Utils.checkHourSlot(advertisement.getHourSlot());
            advertisement.setPublisherUserId(userDTO.getId());
            advertisement.setCandidateUserList(new ArrayList<>());
            //saving and returning advertisementDTO with skillDTO and publisherUserDTO filled
            log.info("Saving advertisement with id: {}", advertisement.getId());
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

    public Page<AdvertisementDTO> getAdvertisementApplicant(HttpServletRequest request, String state, Pageable pageable) {
        log.info("Entering method getAdvertisementApplicant");
        BaseUserDTO user = userService.getUserById(firebaseService.getFirebaseToken(request).getUid());
        String userId = user.getId();
        Query query = new Query();
        query.addCriteria(Criteria.where("candidateUserList").in(userId));
        Page<AdvertisementDTO> pageLavoratore = advertisementRepository.findAll(query, pageable).map(a -> a.toDTOLavoratore(userId)).map(this::finalizeAdvertisementDTO);

        List<AdvertisementDTO> contentLavoratore = pageLavoratore.getContent();
        contentLavoratore = getAdvertisementDTOS(state, contentLavoratore);
        contentLavoratore.stream().filter(a -> a.getCandidateUserList().contains(userId)).collect(Collectors.toList());



        return new PageImpl<>(contentLavoratore, pageable, pageLavoratore.getTotalElements());
    }

    public Page<AdvertisementDTO> getPageFiltered(HttpServletRequest request, String state, Query query, Pageable pageable) {
        log.info("Entering method getPageFiltered");
        log.info("Query: {}", query);
        BaseUserDTO user = userService.getUserById(firebaseService.getFirebaseToken(request).getUid());
        String userRole = user.getRoleId();
        String userId = user.getId();
        switch (userRole) {
            case UserType.DATORE:
                query.addCriteria(Criteria.where("publisherUserId").is(userId));
                return advertisementRepository.findAll(query, pageable).map(Advertisement::toDTO).map(this::finalizeAdvertisementDTO);
            case UserType.LAVORATORE:
                Page<AdvertisementDTO> pageLavoratore = advertisementRepository.findAll(query, pageable).map(a -> a.toDTOLavoratore(userId)).map(this::finalizeAdvertisementDTO);
                List<AdvertisementDTO> contentLavoratore = pageLavoratore.getContent();
                contentLavoratore = getAdvertisementDTOS(state, contentLavoratore);
                return new PageImpl<>(contentLavoratore, pageable, pageLavoratore.getTotalElements());
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
    public AdvertisementDTO matchUser(HttpServletRequest request, String userId, String advertisementId) {
        if (!StringUtils.hasLength(userId) || !StringUtils.hasLength(advertisementId)) {
            log.error("User id or advertisement id is null, check your request");
            return null;
        }
        Optional<Advertisement> opt = advertisementRepository.findById(advertisementId);
        if (opt.isPresent()) {
            Advertisement advertisement = opt.get();
            String userRequestId = firebaseService.getFirebaseToken(request).getUid();
            if (advertisement.getPublisherUserId().equals(userRequestId)) {
                List<String> candidateUserList = advertisement.getCandidateUserList();
                if (!candidateUserList.contains(userId)) {
                    log.error("Cannot match a user that is not in the candidate list");
                } else {
                    // candidateUserList.remove(userId);
                    advertisement.setCandidateUserList(candidateUserList);
                    advertisement.setMatchedUserId(userId);
                    log.info("Matched user {} with advertisement {}", userId, advertisementId);
                    advertisementRepository.save(advertisement);
                    return finalizeAdvertisementDTO(advertisement.toDTO());
                }
            } else {
                log.error("User {} not authorized to match user for advertisement {}", userRequestId, advertisementId);
            }
        } else {
            log.error("Advertisement {} not found", advertisementId);
        }
        return null;
    }
    public AdvertisementDTO candidateUser(HttpServletRequest request, String advertisementId) {
        if (!StringUtils.hasLength(advertisementId)) {
            log.error("Advertisement id is null, check your request");
            return null;
        }
        Optional<Advertisement> opt = advertisementRepository.findById(advertisementId);
        if (opt.isPresent()) {
            Advertisement advertisement = opt.get();
            String userId = firebaseService.getFirebaseToken(request).getUid();
            List<String> candidateUserList = advertisement.getCandidateUserList();
            log.info("User list of advertisement {}: {}", advertisementId, candidateUserList);
            log.info("Number of candidates: {}", candidateUserList.size());
            if (candidateUserList.contains(userId)) {
                //remove user from candidate list if it is present
                log.info("User {} already in candidate list, removing it", userId);
                candidateUserList.remove(userId);
            } else {
                log.info("Candidate user {} in advertisement {}", userId, advertisementId);
                candidateUserList.add(userId);
            }
            advertisement.setCandidateUserList(candidateUserList);
            advertisementRepository.save(advertisement);
            return finalizeAdvertisementDTO(advertisement.toDTO());
        } else {
            log.error("Advertisement {} not found", advertisementId);
        }
        return null;
    }
    public List<AdvertisementDTO> getAllAdvertisementsBySkill(String skillName) {
        log.info("Get all advertisements by skill");
        return getAllAdvertisements().stream().filter(a -> a.getSkillDTO().getName().equals(skillName)).collect(Collectors.toList());
    }
    private List<AdvertisementDTO> getAdvertisementDTOS(String state, List<AdvertisementDTO> contentLavoratore) {
        if (Objects.equals(state, Utils.AdvertisementLavoratoreState.ALL)) {
            contentLavoratore = contentLavoratore.stream()
                    .filter(a ->
                            !a.getAdvertisementStatus().equals(Utils.AdvertisementLavoratoreState.HISTORY) &&
                                    !a.getAdvertisementStatus().equals(Utils.AdvertisementLavoratoreState.IGNORED))
                    .collect(Collectors.toList());
        } else {
            contentLavoratore = contentLavoratore.stream()
                    .filter(a -> a.getAdvertisementStatus().equals(state))
                    .collect(Collectors.toList());
        }
        return contentLavoratore;
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
