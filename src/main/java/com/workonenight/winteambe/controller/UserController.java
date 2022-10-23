package com.workonenight.winteambe.controller;

import com.workonenight.winteambe.common.FilterCondition;
import com.workonenight.winteambe.common.GenericFilterCriteriaBuilder;
import com.workonenight.winteambe.common.PageResponse;
import com.workonenight.winteambe.dto.BaseUserDTO;
import com.workonenight.winteambe.dto.CanIDTO;
import com.workonenight.winteambe.dto.UserDTO;
import com.workonenight.winteambe.service.UserService;
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
    public List<BaseUserDTO> getAllUser(HttpServletRequest request) {
        return userService.getAllUser(request);
    }

    /**
     * Get user by id
     *
     * @param id id of user
     * @return BaseUserDTO
     */
    @GetMapping(value = "/list/{id}")
    public BaseUserDTO getUserById(@PathVariable("id") String id) {
        return userService.getUserById(id);
    }

    /**
     * Create user
     *
     * @param userDTO UserDTO
     * @return UserDTO
     */
    @PostMapping(value = "/create")
    public BaseUserDTO createUser(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    /**
     * Register user
     *
     * @param request HttpServletRequest
     * @return UserDTO
     */
    @GetMapping(value = "/register")
    public BaseUserDTO registerUser(HttpServletRequest request, @RequestParam("role") String role) {
        return userService.registerUser(request, role);
    }

    /**
     * Update user
     *
     * @param userDTO UserDTO
     * @return UserDTO
     */
    @PostMapping(value = "/update")
    public BaseUserDTO updateUser(@RequestBody UserDTO userDTO) {
        return userService.updateUser(userDTO);
    }

    /**
     * Get current user
     *
     * @param request HttpServletRequest
     * @return UserDTO
     */
    @GetMapping(value = "/me")
    public BaseUserDTO getMe(HttpServletRequest request) {
        return userService.getMe(request);
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
    public ResponseEntity<PageResponse<BaseUserDTO>> getSearchCriteriaPage(
            HttpServletRequest request,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "filterOr", required = false) String filterOr,
            @RequestParam(value = "filterAnd", required = false) String filterAnd,
            @RequestParam(value = "orders", required = false) String orders) {

        PageResponse<BaseUserDTO> response = new PageResponse<>();

        Pageable pageable = filterBuilderService.getPageable(size, page, orders);
        GenericFilterCriteriaBuilder filterCriteriaBuilder = new GenericFilterCriteriaBuilder();


        List<FilterCondition> andConditions = filterBuilderService.createFilterCondition(filterAnd);
        List<FilterCondition> orConditions = filterBuilderService.createFilterCondition(filterOr);

        Query query = filterCriteriaBuilder.addCondition(andConditions, orConditions);
        Page<BaseUserDTO> pg = userService.getPageFiltered(request, query, pageable);
        response.setPageStats(pg, pg.getContent());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * @param filterOr  string filter or conditions
     * @param filterAnd string filter and conditions
     * @return list of UserDTO
     */
    @GetMapping("/list/filter")
    public ResponseEntity<List<UserDTO>> getAllSearchCriteria(
            @RequestParam(value = "filterOr", required = false) String filterOr,
            @RequestParam(value = "filterAnd", required = false) String filterAnd) {

        GenericFilterCriteriaBuilder filterCriteriaBuilder = new GenericFilterCriteriaBuilder();

        List<FilterCondition> andConditions = filterBuilderService.createFilterCondition(filterAnd);
        List<FilterCondition> orConditions = filterBuilderService.createFilterCondition(filterOr);

        Query query = filterCriteriaBuilder.addCondition(andConditions, orConditions);
        List<UserDTO> employees = userService.getAllFiltered(query);

        return new ResponseEntity<>(employees, HttpStatus.OK);
    }


    @GetMapping("/cani")
    public ResponseEntity<CanIDTO> canI(HttpServletRequest request, @RequestParam("what") String what) {
        return new ResponseEntity<>(userService.canI(request, what), HttpStatus.OK);
    }
}
