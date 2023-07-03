package com.lcsdev.interfoodhelper.repository;

import com.lcsdev.interfoodhelper.model.ConsumerResponse;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConsumerResponseRepository extends MongoRepository<ConsumerResponse, String> {
}
