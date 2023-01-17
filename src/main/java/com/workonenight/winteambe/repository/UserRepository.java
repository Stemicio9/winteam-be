package com.workonenight.winteambe.repository;

import com.workonenight.winteambe.common.ResourceRepository;
import com.workonenight.winteambe.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends ResourceRepository<User, String> {

    Boolean existsByEmail(String email);

    List<User> findAllByRoleId(String roleId);
}
