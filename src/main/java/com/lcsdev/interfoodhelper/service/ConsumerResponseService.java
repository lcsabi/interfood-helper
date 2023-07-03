package com.lcsdev.interfoodhelper.service;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import com.lcsdev.interfoodhelper.model.ConsumerResponse;
import com.lcsdev.interfoodhelper.model.DailyMeals;
import com.lcsdev.interfoodhelper.model.Meal;
import com.lcsdev.interfoodhelper.repository.ConsumerResponseRepository;
import com.lcsdev.interfoodhelper.util.InterfoodConfiguration;
import com.lcsdev.interfoodhelper.util.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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

    @Scheduled(fixedDelay = 20000) // 60000 = runs every minute
    public void processIncompleteResponses() {
        System.out.println("Scheduled task started");
        List<ConsumerResponse> incompleteResponses = findIncompleteResponses();
        System.out.println("Responses in queue: " + incompleteResponses.size());
        for (ConsumerResponse response : incompleteResponses) {
            // Process incomplete response and compute daily meals
            if (response.getStatus() == ResponseStatus.PROCESSING) {
                String responseId = response.getId();
                System.out.println("[" + responseId + "] STARTED processing");
                List<LocalDate> requestedDates = response.getRequestedDates();

                // Parse Interfood API for requested days
                CompletableFuture<List<DailyMeals>> computedDailyMeals = processDailyMeals(requestedDates);

                // Update the response with the computed daily meals - BLOCKING POINT
                computedDailyMeals.thenAccept(result -> {
                    response.setDailyMeals(result);
                    response.setStatus(ResponseStatus.COMPLETED);
                    responseRepository.save(response);
                    System.out.println("[" + responseId + "] FINISHED processing");
                });

            } else {
                // Response is incomplete but marked as completed, shouldn't happen
                System.out.println("[" + response.getId() + "] Incomplete but marked as completed");
            }
        }
    }

    private CompletableFuture<List<DailyMeals>> processDailyMeals(List<LocalDate> requestedDates) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        List<DailyMeals> computedDailyMeals = new ArrayList<>();

        for (LocalDate date : requestedDates) {
            DailyMeals dailyMeals = DailyMeals.builder()
                    .date(date)
                    .build();

            if (mealService.mealsExistForDay(date)) {
                System.out.println("Records exist for " + date);
                dailyMeals.setMeals(mealService.findByDate(date));
                computedDailyMeals.add(dailyMeals);
            } else {
                System.out.println("Records don't exist for " + date);
                System.out.println("Calling parser function");

                // Get food data for a single day
                CompletableFuture<Void> interfoodFuture = sendInterfoodRequest(date)
                        .thenRun(() -> dailyMeals.setMeals(mealService.findByDate(date)))
                        .thenRun(() -> computedDailyMeals.add(dailyMeals));

                futures.add(interfoodFuture);
            }
        }

        // Wait for all request to complete - BLOCKING POINT
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApplyAsync(v -> computedDailyMeals);
    }

    private CompletableFuture<List<Void>> sendInterfoodRequest(LocalDate date) {
        HttpClient httpClient = HttpClient.newHttpClient();
        String dayQueryString = "d=" + date;
        String languageQueryString = "l=hu";
        String endpoint = interfoodConfiguration.getEndpoint();
        String[] foodCodes = interfoodConfiguration.getFoodCodes();

        List<CompletableFuture<Void>> mealFutures = new ArrayList<>();

        for (String foodCode : foodCodes) {
            String foodCodeQueryString = "k=" + foodCode;

            CompletableFuture<Void> mealFuture = CompletableFuture.runAsync(() -> {
                try {
                    // Build Interfood API call request parameters
                    HttpRequest getRequest = HttpRequest.newBuilder()
                            .uri(new URI(String.join(
                                    "&",
                                    endpoint,
                                    foodCodeQueryString,
                                    dayQueryString,
                                    languageQueryString)))
                            .GET()
                            .build();
                    HttpResponse<String> getResponse = httpClient.send(getRequest,
                            HttpResponse.BodyHandlers.ofString());
                    //System.out.println(date + "-" + foodCode + " request sent.");

                    // Parse responses with Groovy
                    parseInterfoodResponse(date, foodCode, getResponse);
                } catch (IOException | URISyntaxException | InterruptedException e) {
                    throw new RuntimeException("Error during processing API request: ", e);
                }
            });

            mealFutures.add(mealFuture);
        }

        // Wait for all parsing to be done - BLOCKING POINT
        return CompletableFuture.allOf(mealFutures.toArray(new CompletableFuture[0]))
                .thenApplyAsync(v -> mealFutures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()));
    }

    private void parseInterfoodResponse(LocalDate day, String foodCode, HttpResponse<String> response) {
        // Bind Groovy into business logic
        Binding binding = new Binding();
        binding.setVariable("htmlResponse", response.body());

        // Use GroovyShell to parse .html response
        GroovyShell shell = new GroovyShell(binding);
        try {
            shell.evaluate(new File(interfoodConfiguration.getParser()));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Groovy script can't be found at the specified path.");
        }

        // Access database fields from Groovy script
        var dishName = (String) binding.getVariable("dishName");
        var calories = (String) binding.getVariable("calories");
        var protein = (String) binding.getVariable("protein");
        var fat = (String) binding.getVariable("fat");
        var carbs = (String) binding.getVariable("carbs");

        Meal meal = new Meal(null, day, foodCode, dishName, calories, protein, fat, carbs);
        mealService.save(meal);
    }
}
