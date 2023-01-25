package com.workonenight.winteambe.service;

import com.workonenight.winteambe.dto.AdvertisementDTO;
import com.workonenight.winteambe.dto.CanIDTO;
import com.workonenight.winteambe.entity.Advertisement;
import com.workonenight.winteambe.entity.User;
import com.workonenight.winteambe.entity.interfaces.DataTransferObject;
import com.workonenight.winteambe.exception.AdvertisementNotFoundException;
import com.workonenight.winteambe.exception.BadRequestException;
import com.workonenight.winteambe.exception.UnauthorizedUserException;
import com.workonenight.winteambe.exception.UserNotAuthenticatedException;
import com.workonenight.winteambe.repository.AdvertisementRepository;
import com.workonenight.winteambe.utils.UserType;
import com.workonenight.winteambe.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.workonenight.winteambe.utils.Utils.fromHourSlotToHour;

@Slf4j
@Service
public class AdvertisementService {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final SkillService skillService;
    private final AdvertisementRepository advertisementRepository;

    public AdvertisementService(AuthenticationService authenticationService, AdvertisementRepository advertisementRepository, UserService userService, SkillService skillService) {
        this.authenticationService = authenticationService;
        this.advertisementRepository = advertisementRepository;
        this.userService = userService;
        this.skillService = skillService;
    }

    public List<Advertisement> getAllAdvertisements() {
        return advertisementRepository.findAll();
    }

    public Advertisement getAdvertisementById(String id) throws NoSuchElementException {
        log.info("Searching advertisement for id: {}", id);
        return advertisementRepository.findById(id).orElseThrow();
    }

    public Advertisement createAdvertisement(HttpServletRequest request, AdvertisementDTO advertisementDTO) throws UserNotAuthenticatedException {
        log.info("Creating advertisement");
        User user = userService.getMe(request);
        CanIDTO canIDTO = userService.canI(request, "createAdvertisement");
        if (canIDTO.isResponse() && user != null) {
            Advertisement advertisement = advertisementDTO.toEntity();
            advertisement.setId(null);
            Utils.checkHourSlot(advertisement.getHourSlot());
            advertisement.setPublisherUser(user);
            LocalDateTime finalizedDate = LocalDateTime.of(advertisement.getDate().getYear(),
                    advertisement.getDate().getMonth(),
                    advertisement.getDate().getDayOfMonth(),
                    fromHourSlotToHour(advertisement.getHourSlot()),
                    advertisement.getDate().getMinute());
            advertisement.setDate(finalizedDate);
            //saving and returning advertisementDTO with skillDTO and publisherUserDTO filled
            log.info("Saving advertisement...");
            return advertisementRepository.save(advertisement);
        } else {
            log.error("User not found or without appropriate subscription. Can't create advertisement");
            throw new UserNotAuthenticatedException();
        }
    }

    //TODO fix this method
    public Advertisement updateAdvertisement(AdvertisementDTO advertisementDTO) {
      return advertisementRepository.save(advertisementDTO.toEntity());
    }

    public List<Advertisement> getAdvertisementByOwnerAndState(HttpServletRequest request, String state) throws UserNotAuthenticatedException {
        log.info("Entering method getAdvertisementByOwnerAndState");
        User user = userService.getMe(request);
        Query emptyQuery = new Query();
        Query query = Utils.stateToQuery(emptyQuery, state, user.getRoleId(), user.getId());
        query.addCriteria(Criteria.where("publisherUser.id").is(user.getId()));
        return advertisementRepository.findAll(query);
//            List<Advertisement> advertisements = advertisementRepository.findAllByPublisherUserId(userId);
/*            log.info("Found {} advertisements", advertisements.size());
            if (!Objects.equals(state, Utils.AdvertisementDatoreState.ALL)) {
                return advertisements.stream().map(Advertisement::toDTO).filter(advertisementDTO -> advertisementDTO.getAdvertisementStatus().equals(state)).peek(this::finalizeAdvertisementDTO).collect(Collectors.toList());
            } else {
                return advertisements.stream().map(Advertisement::toDTO).peek(this::finalizeAdvertisementDTO).collect(Collectors.toList());
            }

        log.info("No advertisements found");
        return List.of(); */
    }

    public Page<Advertisement> getAdvertisementApplicant(HttpServletRequest request, String state, Pageable pageable) {
        log.info("Entering method getAdvertisementApplicant");
  //      String userId = authenticationService.myInfo(request, FirebaseRequestElement.ID);
        User user = userService.getMe(request);
        Query emptyQuery = new Query();
        Query query = Utils.stateToQuery(emptyQuery, state, user.getRoleId(), user.getId());
        log.info("Query: {}", query);
        return advertisementRepository.findAll(query, pageable);
      //  List<DataTransferObject> filtered = advertisementRepository.findAllByCandidateUserList_Id(userId).stream().map(a ->   Utils.toDto(a, userId, "LAVORATORE")).collect(Collectors.toList());
      //  Page<DataTransferObject> pageLavoratore = advertisementRepository.findAll(query, pageable).map(a ->   Utils.toDto(a, userId, "LAVORATORE"));
      //  List<DataTransferObject> contentLavoratore = pageLavoratore.getContent();
      //  List<DataTransferObject> contentLavoratore = getAdvertisementDTOS(state, filtered);
      //  return new PageImpl<>(contentLavoratore, pageable, contentLavoratore.size());
    }

    public Page<Advertisement> getPageFilteredAndSkilled(HttpServletRequest request, String state, Query firstQuery, Pageable pageable, String skillId){
        log.info("Entering method getPageFiltered");
        User user = userService.getMe(request);
        String userRole = user.getRoleId();
        String userId = user.getId();
        Query query = Utils.stateToQuery(firstQuery, state, user.getRoleId(), user.getId());
        switch (userRole) {
            case UserType.DATORE:
                query.addCriteria(Criteria.where("publisherUser.$id").is(userId));
                return advertisementRepository.findAll(query, pageable);
            case UserType.LAVORATORE:
                query.addCriteria(Criteria.where("skill.id").is(skillId));//todo problema!!!!
                log.info("Query: {}", query);
                Page<Advertisement> pageLavoratore = advertisementRepository.findAll(query, pageable);
                return pageLavoratore;
         /*       List<AdvertisementDTO> contentLavoratore = pageLavoratore.getContent();
                List<AdvertisementDTO> skilled = getAllAdvertisementsBySkill(skillName);
                contentLavoratore = contentLavoratore.stream().filter(skilled::contains).collect(Collectors.toList());
                contentLavoratore = getAdvertisementDTOS(state, contentLavoratore);
                return new PageImpl<>(contentLavoratore, pageable, contentLavoratore.size()); */
            default:
                log.error("User role not valid");
                return null;
        }
    }

    public Page<Advertisement> getPageFiltered(HttpServletRequest request, String state, Query firstQuery, Pageable pageable) {
        log.info("Entering method getPageFiltered");
        log.info("Query: {}", firstQuery);
        User user = userService.getMe(request);
        String userRole = user.getRoleId();
        String userId = user.getId();
        Query query = Utils.stateToQuery(firstQuery, state, user.getRoleId(), user.getId());
        switch (userRole) {
            case UserType.DATORE:
                query.addCriteria(Criteria.where("publisherUser.id").is(userId));
                return advertisementRepository.findAll(query, pageable);
            case UserType.LAVORATORE:
                Page<Advertisement> pageLavoratore = advertisementRepository.findAll(query, pageable);
                return pageLavoratore;
              /*  List<AdvertisementDTO> contentLavoratore = pageLavoratore.getContent();
                contentLavoratore = getAdvertisementDTOS(state, contentLavoratore);
                return new PageImpl<>(contentLavoratore, pageable, pageLavoratore.getTotalElements()); */
            default:
                log.error("User role not valid");
                return null;
        }
    }

    public List<Advertisement> getAllFiltered(Query query) {
        log.info("Get all advertisements filtered");
        return advertisementRepository.findAll(query);
    }
    public Advertisement getAllUsersRelated(String id) {
        Advertisement advertisement = advertisementRepository.findById(id).orElse(null);
        return advertisement;
       /* Advertisement advertisement = advertisementRepository.findById(id).orElse(null);
        if (advertisement != null) {
           return advertisement.getCandidateUserList();
        }
        return new ArrayList<>(); */
    }
    public Advertisement matchUser(HttpServletRequest request, String userId, String advertisementId) {
        if (!StringUtils.hasLength(userId) || !StringUtils.hasLength(advertisementId)) {
            log.error("User id or advertisement id is null, check your request");
            return null;
        }
        Optional<Advertisement> opt = advertisementRepository.findById(advertisementId);
        User user = userService.getMe(request);
        String userRequestId = user.getId();

        User possibleCandidate = userService.findUserById(userId);
        if (opt.isPresent()) {
            Advertisement advertisement = opt.get();
            if (advertisement.getPublisherUser().getId().equals(userRequestId)) {
                List<User> candidateUserList = advertisement.getCandidateUserList();
                if (!candidateUserList.contains(possibleCandidate)) {
                    log.error("Cannot match a user that is not in the candidate list");
                    throw new BadRequestException("Cannot match a user that is not in the candidate list");
                } else {
                    // candidateUserList.remove(userId);
                    //advertisement.setCandidateUserList(candidateUserList);
                    advertisement.setMatchedUser(candidateUserList.stream().filter(u -> u.getId().equals(userId)).findFirst().orElse(null));
                    log.info("Matched user {} with advertisement {}", userId, advertisementId);
                    return advertisementRepository.save(advertisement);
                }
            } else {
                log.error("User {} not authorized to match user for advertisement {}", userRequestId, advertisementId);
                throw new UnauthorizedUserException();
            }
        } else {
            log.error("Advertisement {} not found", advertisementId);
            throw new AdvertisementNotFoundException();
        }
    }
    public Advertisement candidateUser(HttpServletRequest request, String advertisementId) throws AdvertisementNotFoundException{
        if (!StringUtils.hasLength(advertisementId)) {
            log.error("Advertisement id is null, check your request");
            return null;
        }
        Optional<Advertisement> opt = advertisementRepository.findById(advertisementId);
        User user = userService.getMe(request);
        String userId = user.getId();
        if (opt.isPresent()) {
            Advertisement advertisement = opt.get();
            List<User> candidateUserList = advertisement.getCandidateUserList();
            log.info("User list of advertisement {}: {}", advertisementId, candidateUserList);
            log.info("Number of candidates: {}", candidateUserList.size());
            List<User> toChange = candidateUserList.stream().filter(u -> u.getId().equals(userId)).collect(Collectors.toList());
            if (!toChange.isEmpty()) {
                //remove user from candidate list if it is present
                log.info("User {} already in candidate list, removing it", userId);
                candidateUserList.remove(toChange.get(0));
            } else {
                log.info("Candidate user {} in advertisement {}", userId, advertisementId);
                candidateUserList.add(user);
            }
            advertisement.setCandidateUserList(candidateUserList);
            return advertisementRepository.save(advertisement);

        } else {
            log.error("Advertisement {} not found", advertisementId);
            throw new AdvertisementNotFoundException();
        }
    }
    public List<Advertisement> getAllAdvertisementsBySkill(String skillName) {
        log.info("Get all advertisements by skill");
       // return getAllAdvertisements().stream().filter(a -> a.getSkillDTO().getName().equals(skillName)).collect(Collectors.toList());
        return advertisementRepository.findAllBySkill_Name(skillName);
    }




    // todo change this method using directly jpa with right queries
    private List<DataTransferObject> getAdvertisementDTOS(String state, List<DataTransferObject> contentLavoratore) {
        if (Objects.equals(state, Utils.AdvertisementLavoratoreState.ALL)) {
            contentLavoratore = contentLavoratore.stream()
                    .filter(a ->
                            !a.getExtraField("advertisementStatus").equals(Utils.AdvertisementLavoratoreState.HISTORY) &&
                                    !a.getExtraField("advertisementStatus").equals(Utils.AdvertisementLavoratoreState.IGNORED))
                    .collect(Collectors.toList());
        } else {
            contentLavoratore = contentLavoratore.stream()
                    .filter(a -> a.getExtraField("advertisementStatus").equals(state))
                    .collect(Collectors.toList());
        }
        return contentLavoratore;
    }



}
