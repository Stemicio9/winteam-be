package com.workonenight.winteambe.controller;

import com.workonenight.winteambe.common.FilterCondition;
import com.workonenight.winteambe.common.GenericFilterCriteriaBuilder;
import com.workonenight.winteambe.common.PageResponse;
import com.workonenight.winteambe.dto.AdvertisementDTO;
import com.workonenight.winteambe.dto.BaseUserDTO;
import com.workonenight.winteambe.dto.CandidateAdvertisementRequest;
import com.workonenight.winteambe.dto.MatchAdvertisementRequest;
import com.workonenight.winteambe.service.AdvertisementService;
import com.workonenight.winteambe.service.other.FilterBuilderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/advertisement")
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final FilterBuilderService filterBuilderService;


    public AdvertisementController(AdvertisementService advertisementService, FilterBuilderService filterBuilderService) {
        this.advertisementService = advertisementService;
        this.filterBuilderService = filterBuilderService;
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
     * Get all users related to advertisement
     * @return List<UserDTO> List of users related to advertisement
     */
    @GetMapping(value = "/list/users")
    public List<BaseUserDTO> getAllUsersRelated(@RequestParam(name = "id") String id) {
        return advertisementService.getAllUsersRelated(id);
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
    public AdvertisementDTO createAdvertisement(HttpServletRequest request, @RequestBody AdvertisementDTO advertisementDTO) {
        return advertisementService.createAdvertisement(request, advertisementDTO);
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


    @GetMapping(value = "/list/owner")
    public List<AdvertisementDTO> getAdvertisementByOwnerAndState(HttpServletRequest request, @RequestParam("state") String state) {
        return advertisementService.getAdvertisementByOwnerAndState(request, state);
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
    public ResponseEntity<PageResponse<AdvertisementDTO>> getSearchCriteriaPage(
            HttpServletRequest request,
            @RequestParam(value = "state", defaultValue = "all", required = false) String state,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "filterOr", required = false) String filterOr,
            @RequestParam(value = "filterAnd", required = false) String filterAnd,
            @RequestParam(value = "orders", required = false) String orders) {

        PageResponse<AdvertisementDTO> response = new PageResponse<>();

        Pageable pageable = filterBuilderService.getPageable(size, page, orders);
        GenericFilterCriteriaBuilder filterCriteriaBuilder = new GenericFilterCriteriaBuilder();

        List<FilterCondition> andConditions = filterBuilderService.createFilterCondition(filterAnd);
        List<FilterCondition> orConditions = filterBuilderService.createFilterCondition(filterOr);

        Query query = filterCriteriaBuilder.addCondition(andConditions, orConditions);
        Page<AdvertisementDTO> pg = advertisementService.getPageFiltered(request, state, query, pageable);
        response.setPageStats(pg, pg.getContent());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * @param filterOr  string filter or conditions
     * @param filterAnd string filter and conditions
     * @return list of AdvertisementDTO
     */
    @GetMapping("/list/filter")
    public ResponseEntity<List<AdvertisementDTO>> getAllSearchCriteria(
            @RequestParam(value = "filterOr", required = false) String filterOr,
            @RequestParam(value = "filterAnd", required = false) String filterAnd) {

        GenericFilterCriteriaBuilder filterCriteriaBuilder = new GenericFilterCriteriaBuilder();

        List<FilterCondition> andConditions = filterBuilderService.createFilterCondition(filterAnd);
        List<FilterCondition> orConditions = filterBuilderService.createFilterCondition(filterOr);

        Query query = filterCriteriaBuilder.addCondition(andConditions, orConditions);
        List<AdvertisementDTO> advertisementDTOList = advertisementService.getAllFiltered(query);

        return new ResponseEntity<>(advertisementDTOList, HttpStatus.OK);
    }


    @PostMapping(value = "/matched")
    public ResponseEntity<AdvertisementDTO> matchUser(HttpServletRequest request,
                                                      @RequestBody MatchAdvertisementRequest matchAdvertisementRequest) {
        AdvertisementDTO advertisementDTO = advertisementService.matchUser(request, matchAdvertisementRequest.getUserId(), matchAdvertisementRequest.getAdvertisementId());
        return new ResponseEntity<>(advertisementDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/candidate")
    public ResponseEntity<AdvertisementDTO> candidateUser(HttpServletRequest request,
                                                      @RequestBody CandidateAdvertisementRequest candidateAdvertisementRequest) {
        AdvertisementDTO advertisementDTO = advertisementService.candidateUser(request, candidateAdvertisementRequest.getAdvertisementId());
        return new ResponseEntity<>(advertisementDTO, HttpStatus.OK);
    }

}
