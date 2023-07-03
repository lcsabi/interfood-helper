package com.lcsdev.interfoodhelper.repository;

import com.lcsdev.interfoodhelper.model.Meal;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MealRepository extends MongoRepository<Meal, String> {
}
