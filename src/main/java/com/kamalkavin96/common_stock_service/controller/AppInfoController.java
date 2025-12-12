package com.kamalkavin96.common_stock_service.controller;

import com.kamalkavin96.common_stock_service.service.MetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/app")
public class AppInfoController {

    private final Instant startTime = Instant.now();

    @Autowired
    private MetricsService metricsService;


    @GetMapping("/info")
    public Map<String, Object> getInfo() {
        return Map.of(
                "appName", System.getProperty("app.name", "Stock Service"),
                "version", System.getProperty("app.version", "1.0.0"),
                "startedAt", startTime.toString(),
                "currentTime", Instant.now().toString()
        );
    }

    @GetMapping("/metrics")
    public Map<String, Object> getMetrics() {
        return metricsService.getMetrics();
    }
}
