/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.ntth.services;

import java.util.List;

/**
 *
 * @author User
 */
public interface StatsService {

    List<Object[]> getStatsByTime(String time, int year);

}
