package com.lcsdev.interfoodhelper.controller;

import com.lcsdev.interfoodhelper.model.Meal;
import com.lcsdev.interfoodhelper.service.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/meals")
@RequiredArgsConstructor
public class MealController {

    @Autowired
    private final MealService mealService;

    @PostMapping
    public ResponseEntity<String> save(@RequestBody Meal meal) {
        return ResponseEntity.ok(mealService.save(meal));
    }

    @GetMapping
    public ResponseEntity<List<Meal>> findAll() {
        return ResponseEntity.ok(mealService.findAll());
    }

    @GetMapping("/{meal-id}")
    public ResponseEntity<Meal> findById(@PathVariable("meal-id") String mealId) {
        return ResponseEntity.ok(mealService.findById(mealId));
    }

    @DeleteMapping("/{meal-id}")
    public ResponseEntity<Void> delete(@PathVariable("meal-id") String mealId) {
        mealService.delete(mealId);
        return ResponseEntity.accepted().build();
    }
}
