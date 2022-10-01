package com.workonenight.winteambe.repository;

import com.workonenight.winteambe.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {

}
