/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntth.controllers;

import com.ntth.pojo.Company;
import com.ntth.pojo.Job;
import com.ntth.pojo.JobCategory;
import com.ntth.pojo.User;
import com.ntth.repositories.CompanyRepository;
import com.ntth.repositories.JobCategoryRepository;
import com.ntth.repositories.UserRepository;
import com.ntth.services.FollowService;
import com.ntth.services.JobPostingsService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author admin
 */
@Controller
public class JobController {

    @Autowired
    private JobPostingsService jobService;

    @Autowired
    private FollowService followService;

    @Autowired
    //@Qualifier("jobCategoryRepositoryImpl")
    private JobCategoryRepository jobCategoryRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/jobpostings")//chỗ duy nhất tạo đối tượng
    //@Transactional
    public String createView(Model model, Authentication authentication) {
        System.out.println("[DEBUG] Accessing job posting form");
        Job job = new Job();
        job.setCategoryId(new JobCategory());
        List<JobCategory> categories = jobCategoryRepository.getAllCategories();
        if (categories == null) {
            categories = new ArrayList<>();
        }
        model.addAttribute("jobpostings", job);
        model.addAttribute("categories", categories);

        // Lấy userId từ username
        String username = authentication.getName();
        User user = userRepository.getUserByUsername(username);
        if (user == null) {
            System.out.println("[ERROR] No user found for username: " + username);
            return "redirect:/index?error=no_user";
        }

        // Tìm company theo userId
        Company company = companyRepository.findByUserId(user.getId());
        if (company == null) {
            System.out.println("[ERROR] No company found for userId: " + user.getId());
            return "redirect:/index?error=no_company";
        }
        job.setCompanyId(company);
        model.addAttribute("company", company); // Thêm company vào model để jobposting.html có thể truy cập company.id
        return "jobpostings";
    }

    @PostMapping("/add")//nơi gửi đối tượng lên
    //@Transactional
    public String add(@Valid @ModelAttribute("job") Job job, BindingResult result, Model model, Authentication authentication) {
        System.out.println("[DEBUG] Saving job posting");
        if (result.hasErrors()) {
            List<JobCategory> categories = jobCategoryRepository.getAllCategories();
            if (categories == null) {
                categories = new ArrayList<>();
            }
            model.addAttribute("categories", categories);
            return "jobpostings";
        }
        // Gán lại companyId
        String username = authentication.getName();
        User user = userRepository.getUserByUsername(username);
        if (user == null) {
            model.addAttribute("error", "Không tìm thấy người dùng.");
            List<JobCategory> categories = jobCategoryRepository.getAllCategories();
            if (categories == null) {
                categories = new ArrayList<>();
            }
            model.addAttribute("categories", categories);
            return "jobpostings";
        }

        Company company = companyRepository.findByUserId(user.getId());
        if (company == null) {
            model.addAttribute("error", "Không tìm thấy công ty cho người dùng.");
            List<JobCategory> categories = jobCategoryRepository.getAllCategories();
            if (categories == null) {
                categories = new ArrayList<>();
            }
            model.addAttribute("categories", categories);
            return "jobpostings";
        }
        job.setCompanyId(company);//gán trước khi lưu để đảm bảo companyId không null
        job.setCreatedDate(new Date());
        job.setActive(Boolean.TRUE);
//        if (job.getId() != null) {
//            Optional<Job> existingJob = jobService.getJobPostingsById(job.getId());
//            if (existingJob.isEmpty()) {
//                model.addAttribute("error", "Công việc không tồn tại với ID: " + job.getId());
//                List<JobCategory> categories = jobCategoryRepository.getAllCategories();
//                if (categories == null) {
//                    categories = new ArrayList<>();
//                }
//                model.addAttribute("categories", categories);
//                return "jobposting";
//            }
//        }
        this.jobService.createOrUpdate(job);
        followService.notifyCandidates(job);//Gửi mail đến người theo dõi
        return "redirect:/";//đưa trở về trang chủ
    }

    @GetMapping("/jobpostings/{jobpostingsId}")//cap nhat
    //@Transactional
    public String updateView(Model model, @PathVariable(value = "jobpostingsId") int id) {
////        System.out.println("[DEBUG] Editing job posting id: " + id);
//        Optional<Job> jobOpt = jobService.getJobPostingsById(id);
//        if (jobOpt.isEmpty()) {
//            System.out.println("[ERROR] Job not found for id: " + id);
//            model.addAttribute("error", "Công việc không tồn tại với ID: " + id);
//            Job job = new Job();
//            job.setCategoryId(new JobCategory());
//            model.addAttribute("job", job);
//            List<JobCategory> categories = jobCategoryRepository.getAllCategories();
//            if (categories == null) {
//                categories = new ArrayList<>();
//            }
//            model.addAttribute("categories", categories);
//            return "jobpostings";
//        }
//        model.addAttribute("job", jobOpt.get());
//        List<JobCategory> categories = jobCategoryRepository.getAllCategories();
//        if (categories == null) {
//            categories = new ArrayList<>();
//        }
        model.addAttribute("jobpostings", this.jobService.getJobPostingsById(id));
        List<JobCategory> categories = jobCategoryRepository.getAllCategories();
        if (categories == null) {
            categories = new ArrayList<>();
        }
        model.addAttribute("categories", categories);
        return "jobpostings";
    }
}
