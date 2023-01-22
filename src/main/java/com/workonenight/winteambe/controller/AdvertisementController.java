package com.workonenight.winteambe.controller;

import com.workonenight.winteambe.common.FilterCondition;
import com.workonenight.winteambe.common.GenericFilterCriteriaBuilder;
import com.workonenight.winteambe.common.PageResponse;
import com.workonenight.winteambe.dto.AdvertisementDTO;
import com.workonenight.winteambe.dto.request.CandidateAdvertisementRequest;
import com.workonenight.winteambe.dto.request.MatchAdvertisementRequest;
import com.workonenight.winteambe.entity.Advertisement;
import com.workonenight.winteambe.entity.User;
import com.workonenight.winteambe.entity.interfaces.DataTransferObject;
import com.workonenight.winteambe.service.AdvertisementService;
import com.workonenight.winteambe.service.UserService;
import com.workonenight.winteambe.service.other.FilterBuilderService;
import com.workonenight.winteambe.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/advertisement")
@Slf4j
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final UserService userService;
    private final FilterBuilderService filterBuilderService;


    public AdvertisementController(AdvertisementService advertisementService, UserService userService, FilterBuilderService filterBuilderService) {
        this.advertisementService = advertisementService;
        this.userService = userService;
        this.filterBuilderService = filterBuilderService;
    }

    /**
     * Get all advertisements
     * @return List<AdvertisementDTO> List of advertisements
     */
    @GetMapping(value = "/list/all")
    public List<DataTransferObject> getAllAdvertisements() {
        // todo should this be filtered by current user ?????????????????
        return advertisementService.getAllAdvertisements().stream().map(Utils::toDto).collect(Collectors.toList());
    }

    /**
     * Get all users related to advertisement
     * @return List<UserDTO> List of users related to advertisement
     */
    @GetMapping(value = "/list/users")
    public List<DataTransferObject> getAllUsersRelated(HttpServletRequest request, @RequestParam(name = "id") String id) {
        // todo should this be filtered by current user ?????????????????
        User user = userService.getMe(request);
        Advertisement advertisement = advertisementService.getAllUsersRelated(id);
        List<DataTransferObject> relatedUsers = advertisement.getCandidateUserList().stream().map(currentUser -> {
            if(advertisement.getMatchedUser() != null && currentUser.getId().equals(advertisement.getMatchedUser().getId())){
                return Utils.toDto(currentUser, false);
            }else{
                return Utils.toDto(currentUser,  user.getId(), user.getRoleId());
            }
        }).collect(Collectors.toList());
       return relatedUsers;
    }

    @GetMapping(value="/list/skill")
    public ResponseEntity<List<DataTransferObject>> getAdvertisementBySkill( HttpServletRequest request, @RequestParam(name = "skill") String skill) {
        // todo should this be filtered by current user ?????????????????
        List<Advertisement> advertisementDTOList = advertisementService.getAllAdvertisementsBySkill(skill);
        User user = userService.getMe(request);
        return new ResponseEntity<>(advertisementDTOList.stream().map(a -> Utils.toDto(a, user.getId(), user.getRoleId())).collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * Get advertisement by id
     * @param id Advertisement id
     * @return AdvertisementDTO Advertisement
     */
    @GetMapping(value = "/list/{id}")
    public DataTransferObject getAdvertisementById(HttpServletRequest request, @PathVariable("id") String id) {
        // todo should this be filtered by current user ?????????????????
        User user = userService.getMe(request);
        return Utils.toDto(advertisementService.getAdvertisementById(id), user.getId(), user.getRoleId());
    }

    /**
     * Create advertisement
     * @param advertisementDTO AdvertisementDTO
     * @return AdvertisementDTO Advertisement
     */
    @PostMapping(value = "/create")
    public DataTransferObject createAdvertisement(HttpServletRequest request, @RequestBody AdvertisementDTO advertisementDTO) {
        // todo should this be filtered by current user ?????????????????
        User user = userService.getMe(request);
        return Utils.toDto(advertisementService.createAdvertisement(request, advertisementDTO), user.getId(), user.getRoleId());
    }

    /**
     * Update advertisement
     * @param advertisementDTO AdvertisementDTO
     * @return AdvertisementDTO Advertisement
     */
    @PostMapping(value = "/update")
    public DataTransferObject updateAdvertisement(HttpServletRequest request, @RequestBody AdvertisementDTO advertisementDTO) {
        User user = userService.getMe(request);
        return Utils.toDto(advertisementService.updateAdvertisement(advertisementDTO), user.getId(), user.getRoleId());
    }


    @GetMapping(value = "/list/owner")
    public List<DataTransferObject> getAdvertisementByOwnerAndState(HttpServletRequest request, @RequestParam("state") String state) {
        User user = userService.getMe(request);
        return advertisementService.getAdvertisementByOwnerAndState(request, state).stream().map(a-> Utils.toDto(a, user.getId(), user.getRoleId())).collect(Collectors.toList());
    }

    /**
     * @param page      page number
     * @param size      size count
     * @param orders    string orders
     * @return PageResponse<AdvertisementDTO>
     */
    @GetMapping(value = "/page/applicant")
    public ResponseEntity<PageResponse<DataTransferObject>> getSearchCriteriaPageForApplicant(
            HttpServletRequest request,
            @RequestParam(value = "state", defaultValue = "all", required = false) String state,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "orders", required = false) String orders) {

        PageResponse<DataTransferObject> response = new PageResponse<>();
        Pageable pageable = filterBuilderService.getPageable(size, page, orders);
        User user = userService.getMe(request);
        Page<DataTransferObject> pg = Utils.convertPage(advertisementService.getAdvertisementApplicant(request, state, pageable), user.getId(), user.getRoleId());
        response.setPageStats(pg, pg.getContent());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * @param page      page number
     * @param size      size count
     * @param filterOr  string filter or conditions
     * @param filterAnd string filter and conditions
     * @param orders    string orders
     * @return PageResponse<AdvertisementDTO>
     */
    @GetMapping(value = "/page")
    public ResponseEntity<PageResponse<DataTransferObject>> getSearchCriteriaPage(
            HttpServletRequest request,
            @RequestParam(value = "state", defaultValue = "all", required = false) String state,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "filterOr", required = false) String filterOr,
            @RequestParam(value = "filterAnd", required = false) String filterAnd,
            @RequestParam(value = "orders", required = false) String orders,
            @RequestParam(value = "skill", required = false) String skill) {

        PageResponse<DataTransferObject> response = new PageResponse<>();

        Pageable pageable = filterBuilderService.getPageable(size, page, orders);
        GenericFilterCriteriaBuilder filterCriteriaBuilder = new GenericFilterCriteriaBuilder();

        log.error("page = in request" + page);
        log.error("pageSize = " + pageable.getPageSize());


        List<FilterCondition> andConditions = filterBuilderService.createFilterCondition(filterAnd);
        List<FilterCondition> orConditions = filterBuilderService.createFilterCondition(filterOr);

        Query query = filterCriteriaBuilder.addCondition(andConditions, orConditions);
        Page<Advertisement> pg;
        if(StringUtils.isNotBlank(skill)){
             pg = advertisementService.getPageFilteredAndSkilled(request, state, query, pageable, skill);
        }else {
             pg = advertisementService.getPageFiltered(request, state, query, pageable);
        }
        User user = userService.getMe(request);
        response.setPageStats(pg, Utils.convertPage(pg, user.getId(), user.getRoleId()).getContent());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * @param filterOr  string filter or conditions
     * @param filterAnd string filter and conditions
     * @return list of AdvertisementDTO
     */
    @GetMapping("/list/filter")
    public ResponseEntity<List<DataTransferObject>> getAllSearchCriteria(
            HttpServletRequest request,
            @RequestParam(value = "filterOr", required = false) String filterOr,
            @RequestParam(value = "filterAnd", required = false) String filterAnd) {

        GenericFilterCriteriaBuilder filterCriteriaBuilder = new GenericFilterCriteriaBuilder();

        List<FilterCondition> andConditions = filterBuilderService.createFilterCondition(filterAnd);
        List<FilterCondition> orConditions = filterBuilderService.createFilterCondition(filterOr);

        Query query = filterCriteriaBuilder.addCondition(andConditions, orConditions);
        List<Advertisement> advertisementDTOList = advertisementService.getAllFiltered(query);
        User user = userService.getMe(request);
        return new ResponseEntity<>(advertisementDTOList.stream().map(a-> Utils.toDto(a, user.getId(), user.getRoleId())).collect(Collectors.toList()), HttpStatus.OK);
    }


    @PostMapping(value = "/matched")
    public ResponseEntity<DataTransferObject> matchUser(HttpServletRequest request,
                                                      @RequestBody MatchAdvertisementRequest matchAdvertisementRequest) {
        Advertisement advertisementDTO = advertisementService.matchUser(request, matchAdvertisementRequest.getUserId(), matchAdvertisementRequest.getAdvertisementId());
        User user = userService.getMe(request);
        return new ResponseEntity<>(Utils.toDto(advertisementDTO, user.getId(), user.getRoleId()), HttpStatus.OK);
    }

    @PostMapping(value = "/candidate")
    public ResponseEntity<DataTransferObject> candidateUser(HttpServletRequest request,
                                                      @RequestBody CandidateAdvertisementRequest candidateAdvertisementRequest) {
        Advertisement advertisementDTO = advertisementService.candidateUser(request, candidateAdvertisementRequest.getAdvertisementId());
        User user = userService.getMe(request);
        return new ResponseEntity<>(Utils.toDto(advertisementDTO, user.getId(), user.getRoleId()), HttpStatus.OK);
    }

}
