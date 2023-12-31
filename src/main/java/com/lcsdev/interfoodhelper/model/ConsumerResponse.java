package com.lcsdev.interfoodhelper.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcsdev.interfoodhelper.util.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "responses")
@Data
@AllArgsConstructor
@Builder
public class ConsumerResponse {

    @Id
    private String id;
    private LocalDateTime timestamp;
    private ResponseStatus status;

    @JsonProperty("requested_dates")
    private List<LocalDate> requestedDates;

    @JsonProperty("daily_meals")
    private List<DailyMeals> dailyMeals;
}
