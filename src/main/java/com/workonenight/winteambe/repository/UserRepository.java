package com.workonenight.winteambe.repository;

import com.workonenight.winteambe.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
