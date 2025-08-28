/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntth.controllers;

import com.ntth.services.StatsService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author User
 */
@RestController
@RequestMapping("/api/stats")
public class ApiStatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping("/revenue")
    public ResponseEntity<List<Map<String, Object>>> getStats(
            @RequestParam("time") String time,
            @RequestParam("year") int year) {

        List<Object[]> rawStats = statsService.getStatsByTime(time, year);
        List<Map<String, Object>> stats = new ArrayList<>();

        for (Object[] row : rawStats) {
            Map<String, Object> map = new HashMap<>();
            map.put("time", row[0]);              // VD: "Tháng 1" hoặc "Quý 2"
            map.put("jobCount", row[1]);
            map.put("candidateCount", row[2]);
            map.put("employerCount", row[3]);
            stats.add(map);
        }

        return ResponseEntity.ok(stats);
    }
}
