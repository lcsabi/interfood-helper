package com.lcsdev.interfoodhelper.service;

import com.lcsdev.interfoodhelper.model.ConsumerResponse;
import com.lcsdev.interfoodhelper.repository.ConsumerResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class ConsumerResponseService {

    @Autowired
    private final ConsumerResponseRepository responseRepository;

    @Autowired
    private final MealService mealService;

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

    @Scheduled(fixedDelay = 10000) // Runs every minute
    public void processIncompleteResponses() {
        System.out.println("Scheduled task started");
        List<ConsumerResponse> incompleteResponses = findIncompleteResponses();
        for (ConsumerResponse response : incompleteResponses) {
            // Process the incomplete response and compute the daily meals
            // ...
            String responseId = response.getId();
            System.out.println("Processing response " + responseId);
            List<LocalDate> requestedDates = response.getRequestedDates();
            System.out.println("Requested days: " + requestedDates);
            for (LocalDate date : requestedDates) {
                if (mealService.mealsExistForDay(date)) {
                    System.out.println("Records exist for " + date);
                } else {
                    System.out.println("Records don't exist for " + date);
                }
            }
            // Update the response with the computed daily meals
            // response.setDailyMeals(computedDailyMeals);
            // responseRepository.save(response);
        }
    }
}
