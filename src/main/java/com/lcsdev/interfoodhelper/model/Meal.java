package com.lcsdev.interfoodhelper.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private String id;
    @JsonIgnore
    private LocalDate date;
    private String code;
    private String name;
    private String calories;
    private String protein;
    private String fat;
    private String carbs;
}
