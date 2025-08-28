/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntth.controllers;

import com.ntth.pojo.Job;
import com.ntth.pojo.JobApplication;
import static com.ntth.pojo.JobApplication.Status.PENDING;
import com.ntth.pojo.User;
import com.ntth.services.JobApplicationService;
import com.ntth.services.JobPostingsService;
import com.ntth.services.UserService;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author User
 */
@Controller
public class JobApplicationController {

    @Autowired
    private JobApplicationService jobappService;

    @Autowired
    private JobPostingsService jobPostingService;

    @Autowired
    private UserService userService;

//    @GetMapping("/applications/{jobapplicationId}")
//    public String createView(Model model) {
//        model.addAttribute("jobapplication", new JobApplication());
//        return "applications";
//    }
    @GetMapping("/applications/{jobPostingId}")
    public String createView(@PathVariable("jobPostingId") int jobPostingId, Model model) {
        JobApplication jobApp = new JobApplication();

        // Gán ID của jobPost vào JobApplication để giữ lại khi submit
        Job job = jobPostingService.getJobPostingsById(jobPostingId);
        jobApp.setJobId(job);

        model.addAttribute("jobapplication", jobApp);
        return "applications"; // ứng với applications.html
    }

    @PostMapping("/addapplication")
    public String add(@ModelAttribute("jobapplication") JobApplication p, @RequestParam("file") MultipartFile file, Authentication authentication) {
        System.out.println("==== ĐÃ NHẬN ĐƠN ỨNG TUYỂN ====");
        System.out.println("Cover letter: " + p.getCoverLetter());

        if (p.getJobId() != null) {
            System.out.println("Job ID: " + p.getJobId().getId());
        } else {
            System.out.println("Job NULL ❌");
        }
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);

        p.setUserId(user);
        System.out.println("User ID: " + user.getId());

        System.out.println("Resume Link: " + p.getResumeLink());

        try {
            p.setFile(file);
            this.jobappService.addOrUpdateJobApplication(p);
        } catch (Exception ex) {
            ex.printStackTrace(); // In ra lỗi nếu có
            return "error"; // Redirect sang trang báo lỗi nếu cần
        }

        return "redirect:/";
    }

//    @GetMapping("/jobapplications/{jobapplicationId}")
//    public String updateView(Model model, @PathVariable(value = "jobapplication") int id) {
//        model.addAttribute("jobapplication", this.jobappService.getJobApplications(id));
//        
//        return "jobapplications";
//    
//
//    }
//    @GetMapping("/applications/{jobId}")
//    public String addViewApplication(Model model, @PathVariable("jobId") int jobId,
//            Authentication authentication) {
//        JobApplication jobApp = new JobApplication();
//        String username = authentication.getName();
//        User user = userService.getUserByUsername(username);
//        if (user == null) {
//            return "redirect:/index?error=no_user";
//        } else {
//            Job posting = jobPostingService.getJobPostingsById(jobId);
//            jobApp.setUserId(user);
//            jobApp.setJob(posting);
//            // Gán ngày nộp
//            jobApp.setApplicationDate(LocalDateTime.now());
//
//            // Trạng thái ban đầu
//            jobApp.setStatus(PENDING);
//            //   jobappService.addOrUpdateJobApplication(jobApp);
//        }
//        model.addAttribute("jobapplication", jobApp);
//        System.out.println("Đã thêm vào model");
//        return "applications";
//
//    }
//
//    @PostMapping("/addapplication")
//    public String add(@ModelAttribute(value = "jobapplication") JobApplication p, @RequestParam("file") MultipartFile file, Authentication authentication) {
//        try {
//            System.out.println(">>> Dang chay addapplication: ");
//
//            p.setFile(file);
//            System.out.println(">>> COVER LETTER: " + p.getCoverLetter());
//
//            this.jobappService.addOrUpdateJobApplication(p);
//        } catch (Exception ex) {
//            ex.printStackTrace(); // ✅ In lỗi chi tiết
//        }
//        return "redirect:/";
//    }
    // Hiển thị danh sách ứng tuyển
    @GetMapping("/listapplications")
    public String list(Model model, Principal principal
    ) {
        String username = principal.getName();
        User currentUser = userService.getUserByUsername(username);

        if (currentUser == null) {
            return "error/401"; // Trả về trang lỗi nếu user chưa đăng nhập
        }

        if (currentUser.getRole() == User.Role.CANDIDATE) {
            model.addAttribute("applications", jobappService.getJobApplicationsByUserId(currentUser.getId()));
        } else if (currentUser.getRole() == User.Role.EMPLOYER) {
            model.addAttribute("applications", jobappService.getJobApplicationsByEmployerId(currentUser.getId()));
        }

        return "listapplications"; // Trả về trang danh sách JobApplications
    }

    // Hiển thị form cập nhật trạng thái ứng tuyển
    @GetMapping("/update/{id}")
    public String updateView(Model model, @PathVariable(value = "id") int id
    ) {
        model.addAttribute("application", jobappService.getJobApplicationById(id));
        return "jobapplication-update"; // Trả về trang cập nhật JobApplication
    }

    // Cập nhật trạng thái ứng tuyển (Từ chối hoặc chấp nhận)
    @PostMapping("/update")
    public String update(@ModelAttribute("application") JobApplication application
    ) {
        jobappService.addOrUpdateJobApplication(application);
        return "redirect:/jobapplications"; // Quay lại danh sách sau khi cập nhật
    }

    // Xử lý từ chối ứng tuyển
    @PostMapping("/jobapplications/reject/{id}")
    public String reject(@PathVariable(value = "id") int id
    ) {
        JobApplication application = jobappService.getJobApplicationById(id);
        if (application != null) {
            application.setStatus(JobApplication.Status.REJECTED);
            jobappService.addOrUpdateJobApplication(application);
        }
        return "redirect:/listapplications";
    }

    // Xử lý chấp nhận ứng tuyển
    @PostMapping("/jobapplications/accept/{id}")
    public String accept(@PathVariable(value = "id") int id
    ) {
        JobApplication application = jobappService.getJobApplicationById(id);
        if (application != null) {
            application.setStatus(JobApplication.Status.INTERVIEW);
            jobappService.addOrUpdateJobApplication(application);
        }
        return "redirect:/listapplications";
    }

}
