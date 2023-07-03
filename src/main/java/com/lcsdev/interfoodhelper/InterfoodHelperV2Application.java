package com.lcsdev.interfoodhelper;

import com.lcsdev.interfoodhelper.model.Meal;
import com.lcsdev.interfoodhelper.repository.MealRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class InterfoodHelperV2Application {

	public static void main(String[] args) {
		SpringApplication.run(InterfoodHelperV2Application.class, args);
	}

	@Bean
	public CommandLineRunner clr(MealRepository mealRepository) {
		return args -> {
			var meal = Meal.builder()
					.day(LocalDate.of(2023, 7, 4))
					.code("L1")
					.name("Csülkös bableves")
					.calories("747 kcal")
					.protein("35.2g")
					.fat("39.3g")
					.carbs("64.3g")
					.build();
			mealRepository.insert(meal);
		};
	}
}
