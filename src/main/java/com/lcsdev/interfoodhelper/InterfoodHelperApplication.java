package com.lcsdev.interfoodhelper;

import com.lcsdev.interfoodhelper.model.Meal;
import com.lcsdev.interfoodhelper.repository.MealRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class InterfoodHelperApplication {

	public static void main(String[] args) {
		SpringApplication.run(InterfoodHelperApplication.class, args);
	}

	//@Bean
	public CommandLineRunner clr(MealRepository mealRepository) {
		return args -> {
			var meal = Meal.builder()
					.date(LocalDate.of(2023, 7, 4))
					.code("L1")
					.name("Csülkös bableves")
					.calories("747 kcal")
					.protein("35.2g")
					.fat("39.3g")
					.carbs("64.3g")
					.build();
			mealRepository.insert(meal);
			meal = Meal.builder()
					.date(LocalDate.of(2023, 7, 4))
					.code("A")
					.name("Rácpörkölt")
					.calories("958 kcal")
					.protein("49.8g")
					.fat("47g")
					.carbs("86.2g")
					.build();
			mealRepository.insert(meal);
			meal = Meal.builder()
					.date(LocalDate.of(2023, 7, 5))
					.code("O")
					.name("Sólet, füstölt tarja")
					.calories("861 kcal")
					.protein("52.4g")
					.fat("30.5g")
					.carbs("94.6g")
					.build();
			mealRepository.insert(meal);
		};
	}
}
