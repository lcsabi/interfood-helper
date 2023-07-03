package com.lcsdev.interfoodhelper.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "meals")
@Data
@AllArgsConstructor
@Builder
public class Meal {

    @Id
    private String id;
    private LocalDate day;
    private String code;
    private String name;
    private String calories;
    private String protein;
    private String fat;
    private String carbs;
}
