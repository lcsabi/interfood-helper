package com.lcsdev.interfoodhelper.service;

import com.lcsdev.interfoodhelper.model.ConsumerResponse;
import com.lcsdev.interfoodhelper.repository.ConsumerResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsumerResponseService {

    @Autowired
    private ConsumerResponseRepository responseRepository;

    public String save(ConsumerResponse consumerResponse) {
        return responseRepository.save(consumerResponse).getId();
    }

    public ConsumerResponse findById(String id) {
        return responseRepository.findById(id)
                .orElseThrow();
    }

    public List<ConsumerResponse> findAll() {
        return responseRepository.findAll();
    }

    public void delete(String id) {
        responseRepository.deleteById(id);
    }

    public List<ConsumerResponse> findIncompleteResponses() {
        return responseRepository.findByDailyMealsIsNull();
    }
}
