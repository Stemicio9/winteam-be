package com.workonenight.winteambe.controller;

import com.workonenight.winteambe.common.FilterCondition;
import com.workonenight.winteambe.common.GenericFilterCriteriaBuilder;
import com.workonenight.winteambe.common.PageResponse;
import com.workonenight.winteambe.dto.BaseUserDTO;
import com.workonenight.winteambe.dto.CanIDTO;
import com.workonenight.winteambe.dto.response.SubscriptionResponse;
import com.workonenight.winteambe.entity.interfaces.DataTransferObject;
import com.workonenight.winteambe.service.UserService;
import com.workonenight.winteambe.service.other.FilterBuilderService;
import com.workonenight.winteambe.utils.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;
    private final FilterBuilderService filterBuilderService;

    public UserController(UserService userService, FilterBuilderService filterBuilderService) {
        this.userService = userService;
        this.filterBuilderService = filterBuilderService;
    }

    /**
     * Get all users
     *
     * @return List<BaseUserDTO>
     */
    @GetMapping(value = "/list/all")
    public List<DataTransferObject> getAllUser() {
        try {
            return userService.findAllUsers().stream().map(Utils::toDto).collect(Collectors.toList());
        } catch (Exception e) {
            // for now we catch generic exception
            return null;
        }
    }

    /**
     * Get user by id
     *
     * @param id id of user
     * @return BaseUserDTO
     */
    @GetMapping(value = "/list/{id}")
    public DataTransferObject getUserById(@PathVariable("id") String id) {
        return Utils.toDto(userService.findUserById(id));
    }

    /**
     * Create user
     *
     * @param userDTO UserDTO
     * @return UserDTO
     */
    @PostMapping(value = "/create")
    public DataTransferObject createUser(@RequestBody BaseUserDTO userDTO) {
        return Utils.toDto(userService.createUser(userDTO.toEntity()));
    }

    /**
     * Register user
     *
     * @param request HttpServletRequest
     * @return UserDTO
     */
    @GetMapping(value = "/register")
    public DataTransferObject registerUser(HttpServletRequest request, @RequestParam("role") String role) {
        return Utils.toDto(userService.registerUser(request, role));
    }

    /**
     * Update user
     *
     * @param userDTO UserDTO
     * @return UserDTO
     */
    @PostMapping(value = "/update")
    public DataTransferObject updateUser(@RequestBody BaseUserDTO userDTO) {
        return Utils.toDto(userService.updateUser(userDTO.toEntity()));
    }

    /**
     * Get current user
     *
     * @param request HttpServletRequest
     * @return UserDTO
     */
    @GetMapping(value = "/me")
    public DataTransferObject getMe(HttpServletRequest request) {
        return Utils.toDto(userService.getMe(request), false);
    }


    /**
     * @param page      page number
     * @param size      size count
     * @param filterOr  string filter or conditions
     * @param filterAnd string filter and conditions
     * @param orders    string orders
     * @return PageResponse<UserDTO>
     */
    @GetMapping(value = "/page")
    public ResponseEntity<PageResponse<DataTransferObject>> getSearchCriteriaPage(
            HttpServletRequest request,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "filterOr", required = false) String filterOr,
            @RequestParam(value = "filterAnd", required = false) String filterAnd,
            @RequestParam(value = "orders", required = false) String orders) {

        PageResponse<DataTransferObject> response = new PageResponse<>();

        Pageable pageable = filterBuilderService.getPageable(size, page, orders);
        GenericFilterCriteriaBuilder filterCriteriaBuilder = new GenericFilterCriteriaBuilder();


        List<FilterCondition> andConditions = filterBuilderService.createFilterCondition(filterAnd);
        List<FilterCondition> orConditions = filterBuilderService.createFilterCondition(filterOr);

        Query query = filterCriteriaBuilder.addCondition(andConditions, orConditions);
        Page<DataTransferObject> pg = userService.getPageFiltered(request, query, pageable).map(Utils::toDto);
        response.setPageStats(pg, pg.getContent());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * @param filterOr  string filter or conditions
     * @param filterAnd string filter and conditions
     * @return list of UserDTO
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
        List<DataTransferObject> employees = userService.getAllFiltered(query, Pageable.ofSize(1000)).getContent().stream().map(Utils::toDto).collect(Collectors.toList());

        return new ResponseEntity<>(employees, HttpStatus.OK);
    }


    @GetMapping("/cani")
    public ResponseEntity<CanIDTO> canI(HttpServletRequest request, @RequestParam("what") String what) {
        return new ResponseEntity<>(userService.canI(request, what), HttpStatus.OK);
    }

    @GetMapping("/mysub")
    public ResponseEntity<SubscriptionResponse> mySubscription(HttpServletRequest request) {
        return new ResponseEntity<>(userService.mySubscription(request), HttpStatus.OK);
    }

    @GetMapping("/search/list")
    public ResponseEntity<Page<DataTransferObject>> searchUser(@RequestParam(value = "page", defaultValue = "0") int page,
                                                   @RequestParam(value = "size", defaultValue = "20") int size,
                                                   @RequestParam("search") String search) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(Utils.convertPage(userService.searchUser(search, pageable)), HttpStatus.OK);
    }
}
