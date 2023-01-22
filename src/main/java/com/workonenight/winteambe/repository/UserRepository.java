package com.workonenight.winteambe.repository;

import com.workonenight.winteambe.common.ResourceRepository;
import com.workonenight.winteambe.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends ResourceRepository<User, String> {

    Boolean existsByEmail(String email);

    List<User> findAllByRoleId(String roleId);

    Page<User> findAllByIdNotAndRoleIdNot(String id, String roleId, Pageable pageable);


    Page<User> findAllByRoleIdAndFirstNameContainsOrRoleIdAndLastNameContainsOrRoleIdAndSkillList_Id(String roleId, String firstName,
                                                                                                               String roleId2, String lastName,
                                                                                                               String roleId3,  String skillId,
                                                                                                               Pageable pageable);

}
