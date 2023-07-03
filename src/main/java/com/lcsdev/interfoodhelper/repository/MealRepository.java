package com.lcsdev.interfoodhelper.repository;

import com.lcsdev.interfoodhelper.model.Meal;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MealRepository extends MongoRepository<Meal, String> {

    Optional<List<Meal>> findByDate(LocalDate date);
}
