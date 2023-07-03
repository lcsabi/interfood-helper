package com.lcsdev.interfoodhelper.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class DailyMeals {

    private LocalDate date;
    private List<Meal> meals;
}
