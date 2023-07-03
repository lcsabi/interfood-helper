package com.lcsdev.interfoodhelper.util;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "interfood")
public class InterfoodConfiguration {

    private String endpoint;
    private String[] foodCodes;
    private String parser;
}
