// /*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.ntth.controllers;
//
//import com.ntth.services.JobPostingsService;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import com.ntth.pojo.Job;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// *
// * @author admin
// */
//@RestController
//@RequestMapping("/api")
//@CrossOrigin
//public class ApiJobPostingController {
//    @Autowired
//    private JobPostingsService jobService;
//
//    @DeleteMapping("/jobpostings/{id}")
//    public ResponseEntity<Void> destroy(@PathVariable("id") int id) {
//        this.jobService.deleleJobPostings(id);
//        return ResponseEntity.noContent().build();
//    }
//    @GetMapping("/jobpostings")
//public ResponseEntity<Map<String, Object>> getJobPostings(
//    @RequestParam Map<String, String> params) {
//    Map<String, Object> response = new HashMap<>();
////    List<Job> jobs = jobService.getJobPostingsById(params.getId());
////    int totalPages = jobService.getTotalPages(params);
////    response.put("jobs", jobs);
////    response.put("totalPages", totalPages);
//    return ResponseEntity.ok(response);
//}
//}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntth.controllers;

import com.ntth.pojo.Company;
import com.ntth.services.JobPostingsService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.ntth.pojo.Job;
import com.ntth.pojo.JobCategory;
import com.ntth.pojo.User;
import com.ntth.repositories.CompanyRepository;
import com.ntth.repositories.UserRepository;
import com.ntth.services.FollowService;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author admin
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiJobPostingController {

    @Autowired
    private JobPostingsService jobService;

    @Autowired
    private FollowService followService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @DeleteMapping("/jobpostings/{id}")
    public ResponseEntity<Void> destroy(@PathVariable("id") int id) {
        this.jobService.deleleJobPostings(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<Job>> getJobPostings(@RequestParam Map<String, String> params) {
        List<Job> jobs = jobService.getJob(params);
        
        // Tính totalPages
        long totalRecords = jobService.countJobs(params);
        int totalPages = (int) Math.ceil((double) totalRecords / 6); // PAGE_SIZE = 6

        // Thêm totalPages vào header
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Pages", String.valueOf(totalPages));
        
        return new ResponseEntity<>(jobs, headers, HttpStatus.OK);
    }

    @PostMapping(value = "/createjob", consumes = {"application/json", "application/json;charset=UTF-8"})
    @PreAuthorize("hasRole('EMPLOYER')") // Chỉ cho phép EMPLOYER truy cập
    public ResponseEntity<Map<String, Object>> createJob(
            @RequestBody Job jobRequest,
            Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Lấy thông tin người dùng từ Authentication
            String username = authentication.getName();
            User user = userRepository.getUserByUsername(username);
            if (user == null) {
                response.put("success", false);
                response.put("message", "Không tìm thấy người dùng.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Kiểm tra công ty của người dùng
            Company company = companyRepository.findByUserId(user.getId());
            if (company == null) {
                response.put("success", false);
                response.put("message", "Không tìm thấy công ty cho người dùng.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (!company.getApproved()) {
                response.put("success", false);
                response.put("message", "Công ty chưa được phê duyệt.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            // Tạo đối tượng Job từ request
            Job job = new Job();
            job.setTitle(jobRequest.getTitle());
            job.setDescription(jobRequest.getDescription());
            job.setSkills(jobRequest.getSkills());
            job.setSalaryFrom(jobRequest.getSalaryFrom());
            job.setSalaryTo(jobRequest.getSalaryTo());
            job.setWorkingTime(jobRequest.getWorkingTime());
            job.setLocation(jobRequest.getLocation());
            job.setLocationLink(jobRequest.getLocationLink());
            job.setCategoryId(new JobCategory(jobRequest.getCategoryId().getId()));
            job.setCompanyId(company);
            job.setCreatedDate(new Date());
            job.setActive(true);

            // Lưu job vào cơ sở dữ liệu
            Job createdJob = jobService.createOrUpdate(job);

            // Gửi thông báo cho ứng viên theo dõi
            followService.notifyCandidates(createdJob);

            // Trả về phản hồi
            response.put("success", true);
            response.put("message", "Đăng bài công việc thành công!");
            response.put("job", createdJob);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi đăng bài công việc: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}