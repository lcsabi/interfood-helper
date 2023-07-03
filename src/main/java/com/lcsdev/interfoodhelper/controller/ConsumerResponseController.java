package com.lcsdev.interfoodhelper.controller;

import com.lcsdev.interfoodhelper.model.ConsumerRequest;
import com.lcsdev.interfoodhelper.model.ConsumerResponse;
import com.lcsdev.interfoodhelper.service.ConsumerResponseService;
import com.lcsdev.interfoodhelper.util.ResponseStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ConsumerResponseController {

    @Autowired
    private final ConsumerResponseService responseService;

    @PostMapping
    public ResponseEntity<String> saveRequest(@RequestBody ConsumerRequest request) {
        ConsumerResponse response = ConsumerResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(ResponseStatus.PROCESSING)
                .requestedDays(request.getDates())
                .dailyMeals(null)
                .build();
        return ResponseEntity.ok(responseService.save(response));
    }

    @GetMapping
    public ResponseEntity<List<ConsumerResponse>> findAll() {
        return ResponseEntity.ok(responseService.findAll());
    }
}
