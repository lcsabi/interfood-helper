package com.lcsdev.interfoodhelper.service;

import com.lcsdev.interfoodhelper.model.ConsumerResponse;
import com.lcsdev.interfoodhelper.model.DailyMeals;
import com.lcsdev.interfoodhelper.repository.ConsumerResponseRepository;
import com.lcsdev.interfoodhelper.util.InterfoodConfiguration;
import com.lcsdev.interfoodhelper.util.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class ConsumerResponseService {

    @Autowired
    private final ConsumerResponseRepository responseRepository;

    @Autowired
    private final MealService mealService;

    @Autowired
    private final InterfoodConfiguration interfoodConfiguration;

    public ConsumerResponse save(ConsumerResponse consumerResponse) {
        responseRepository.save(consumerResponse);
        return consumerResponse;
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

    @Scheduled(fixedDelay = 10000) // 60000 = runs every minute
    public void processIncompleteResponses() {
        System.out.println("Scheduled task started");
        List<ConsumerResponse> incompleteResponses = findIncompleteResponses();
        System.out.println("Incomplete responses in queue: " + incompleteResponses.size());
        for (ConsumerResponse response : incompleteResponses) {
            if (response.getStatus() == ResponseStatus.PROCESSING) {
                // Process incomplete response and compute daily meals
                String responseId = response.getId();
                System.out.println("Processing response " + responseId);
                List<LocalDate> requestedDates = response.getRequestedDates();
                System.out.println("Requested days: " + requestedDates);

                // START 1 - CompletableFuture<List<DailyMeals>> cf1
                List<DailyMeals> computedDailyMeals = new ArrayList<>();
                for (LocalDate date : requestedDates) {
                    DailyMeals dailyMeals = DailyMeals.builder()
                            .date(date)
                            .build();
                    if (mealService.mealsExistForDay(date)) {
                        // nothing to do, just need to query data from database, jump after else
                        System.out.println("Records exist for " + date);
                    } else {
                        // START 2 - CompletableFuture<Void> cf2
                        // TODO: Call parser class, upload to database
                        // parser just parses and uploads to database
                        System.out.println("Records don't exist for " + date);
                        System.out.println("Calling parser function");
                        // BLOCK UNTIL HERE
                    }
                    // Query meals from database, add it to current day
                    // might need to put these two in both if and else scopes
                    dailyMeals.setMeals(mealService.findByDate(date));
                    computedDailyMeals.add(dailyMeals);
                }
                // END 1
                // BLOCK UNTIL HERE
                // Update the response with the computed daily meals
                response.setDailyMeals(computedDailyMeals);
                response.setStatus(ResponseStatus.COMPLETED);
                responseRepository.save(response);
            } else {
                // Response is incomplete but marked as completed, shouldn't happen
                System.out.println("Response "+ response.getId() + " is incomplete but marked as completed");
            }
        }
    }

}
