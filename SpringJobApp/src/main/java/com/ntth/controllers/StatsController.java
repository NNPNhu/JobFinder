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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author User
 */
@Controller
@RequestMapping("/stats")
public class StatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping
    public String showStatsPage() {
        return "stats"; // Trả về trang thống kê
    }

    @PostMapping("/revenue")
    public String getStats(@RequestParam("time") String time,
            @RequestParam("year") int year,
            Model model) {
        List<Object[]> rawStats = statsService.getStatsByTime(time, year);
        List<Map<String, Object>> stats = new ArrayList<>();

        for (Object[] row : rawStats) {
            Map<String, Object> map = new HashMap<>();
            map.put("time", row[0]); // Thời gian
            map.put("jobCount", row[1]); // Số lượng Job
            map.put("candidateCount", row[2]); // Số lượng Candidate
            map.put("employerCount", row[3]); // Số lượng Employer
            stats.add(map);
        }
        // Gửi dữ liệu thống kê đến Thymeleaf
        model.addAttribute("stats", stats);
        model.addAttribute("selectedTime", time);
        model.addAttribute("selectedYear", year);

        return "stats"; // Trả về trang với dữ liệu cập nhật
    }
}

//@RestController
//@RequestMapping("/api/stats")
//@CrossOrigin
//public class StatsController {
//
//    @Autowired
//    private StatsService statsService;
//
//    @GetMapping("/revenue")
//    public ResponseEntity<List<Object[]>> getStats(@RequestParam("time") String time,
//                                                   @RequestParam("year") int year) {
//        List<Object[]> stats = statsService.getStatsByTime(time, year);
//        return ResponseEntity.ok(stats);
//    }
//}
//
