package com.lcsdev.interfoodhelper.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class ConsumerRequest {

    private List<LocalDate> parseDates;
}
