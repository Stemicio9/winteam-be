package com.workonenight.winteambe.repository;

import com.workonenight.winteambe.common.ResourceRepository;
import com.workonenight.winteambe.entity.User;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends ResourceRepository<User, String> {
}
