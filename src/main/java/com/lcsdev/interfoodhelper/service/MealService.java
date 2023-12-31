package com.lcsdev.interfoodhelper.service;

import com.lcsdev.interfoodhelper.model.Meal;
import com.lcsdev.interfoodhelper.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MealService {

    @Autowired
    private final MealRepository mealRepository;

    public String save(Meal meal) {
        return mealRepository.save(meal).getId();
    }

    public Meal findById(String id) {
        return mealRepository.findById(id)
                .orElseThrow();
    }

    public List<Meal> findByDate(LocalDate date) {
        return mealRepository.findByDate(date)
                .orElseThrow();
    }

    public boolean mealsExistForDay(LocalDate day) {
        List<Meal> meals = findByDate(day);
        return !meals.isEmpty();
    }

    public List<Meal> findAll() {
        return mealRepository.findAll();
    }

    public void delete(String id) {
        mealRepository.deleteById(id);
    }
}
