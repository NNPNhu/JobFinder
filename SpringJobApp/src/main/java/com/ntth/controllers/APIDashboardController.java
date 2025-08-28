
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntth.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author LOQ
 */
@Controller
public class APIDashboardController {

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "adminDashboard";
    }
}
