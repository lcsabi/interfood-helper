package com.lcsdev.interfoodhelper.repository;

import com.lcsdev.interfoodhelper.model.ConsumerResponse;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ConsumerResponseRepository extends MongoRepository<ConsumerResponse, String> {

    List<ConsumerResponse> findByDailyMealsIsNull();
}
