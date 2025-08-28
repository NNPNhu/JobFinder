/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ntth.repositories;

import java.util.List;

/**
 *
 * @author User
 */
public interface StatsRepository {

    List<Object[]> statsRevenueByTime(String time, int year);
}
