/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntth.controllers;

import com.ntth.pojo.Job;
import com.ntth.pojo.JobCategory;
import com.ntth.repositories.JobCategoryRepository;
import com.ntth.services.JobPostingsService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author LOQ
 */
@Controller
public class HomeController {

    @Autowired
    private JobPostingsService jobService;

    @Autowired
    private JobCategoryRepository jobCategoryRepository;

    @GetMapping("/")
    public String showHomePage(@ModelAttribute("job") Job job,
            @RequestParam(value = "page", defaultValue = "1") String page,
            Model model) {
        if (job.getCategoryId() == null) {
            job.setCategoryId(new JobCategory());
        }
        if (job.getTitle() == null) {
            job.setTitle("");
        }
        Map<String, String> params = new HashMap<>();
        if (job.getTitle() != null && !job.getTitle().trim().isEmpty()) {
            params.put("kw", job.getTitle());
        }
        if (job.getCategoryId() != null && job.getCategoryId().getId() != null) {
            params.put("categoryId", job.getCategoryId().getId().toString());
        }
        if (job.getSalaryFrom() != null) {
            params.put("salaryFrom", job.getSalaryFrom().toString());
        }
        if (job.getSalaryTo() != null) {
            params.put("salaryTo", job.getSalaryTo().toString());
        }
        if (job.getLocation() != null && !job.getLocation().trim().isEmpty()) {
            params.put("location", job.getLocation());
        }
        if (job.getWorkingTime() != null && !job.getWorkingTime().trim().isEmpty()) {
            params.put("workingTime", job.getWorkingTime());
        }
        //tìm google map
//        if (job.getLatitude() != null) {
//            params.put("latitude", job.getLatitude().toString());
//        }
//        if (job.getLongitude() != null) {
//            params.put("longitude", job.getLongitude().toString());
//        }
//        if (job.getRadius() != null) {
//            params.put("radius", job.getRadius().toString());
//        }
        params.put("page", page); // Truyền page từ query parameter

        // Lấy danh sách job trực tiếp từ service
        List<Job> jobList = jobService.getJob(params);
        model.addAttribute("jobpostings", jobList);

        // Tính tổng số trang
        long totalJobs = jobService.countJobs(params);
        int pageSize = 10; // Đảm bảo PAGE_SIZE trong JobPostingsRepositoryImpl là 10 hoặc điều chỉnh tại đây
        int totalPages = (int) Math.ceil((double) totalJobs / pageSize);
        model.addAttribute("currentPage", Integer.parseInt(page));
        model.addAttribute("totalPages", totalPages > 0 ? totalPages : 1);
        model.addAttribute("pageSize", pageSize);

        // Lấy danh sách danh mục
        List<JobCategory> categories = jobCategoryRepository.getAllCategories();
        model.addAttribute("categories", categories != null ? categories : List.of());

        model.addAttribute("job", job);

        return "index";
    }
}