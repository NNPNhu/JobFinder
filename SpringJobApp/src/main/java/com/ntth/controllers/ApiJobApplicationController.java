/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntth.controllers;

import com.ntth.pojo.Feedback;
import com.ntth.pojo.JobApplication;
import com.ntth.pojo.User;
import static com.ntth.pojo.User.Role.CANDIDATE;
import static com.ntth.pojo.User.Role.EMPLOYER;
import com.ntth.services.JobApplicationService;
import com.ntth.services.UserService;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author User
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiJobApplicationController {

    @Autowired
    private JobApplicationService jaService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/jobapplication/reject/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<?> reject(@PathVariable(value = "id") int id) {
        JobApplication application = jaService.getJobApplicationById(id);
        if (application != null) {
            application.setStatus(JobApplication.Status.REJECTED);
            jaService.addOrUpdateJobApplication(application);
            System.out.println("JobApplication updated: " + application);
        }
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/jobapplications/accept/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<?> accept(@PathVariable(value = "id") int id) {
        JobApplication application = jaService.getJobApplicationById(id);
        if (application != null) {
            application.setStatus(JobApplication.Status.INTERVIEW);
            jaService.addOrUpdateJobApplication(application);
            System.out.println("JobApplication updated: " + application);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/JobApplications")
    public ResponseEntity<List<JobApplication>> list(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(this.jaService.getJobApplications(params), HttpStatus.OK);
    }

    //Lấy danh sách ứng tuyển của user hiện tại (theo dõi trạng thái ứng tuyển)
    @GetMapping("/jobapplications")
    public ResponseEntity<List<JobApplication>> list(@RequestParam Map<String, String> params, Principal principal) {
        if (principal == null) {
            System.out.println("Principal is null!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = principal.getName();
        System.out.println("Logged in user: " + username);

        // Lấy user từ username
        User currentUser = userService.getUserByUsername(username);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if (currentUser.getRole() == CANDIDATE) // Lấy danh sách JobApplications của user hiện tại
        {
            List<JobApplication> userApplications = jaService.getJobApplicationsByUserId(currentUser.getId());
            return ResponseEntity.ok(userApplications);

        }
        if (currentUser.getRole() == EMPLOYER) {
            List<JobApplication> employerApplications = jaService.getJobApplicationsByEmployerId(currentUser.getId());
            return ResponseEntity.ok(employerApplications);
        }
        return ResponseEntity.noContent().build();

    }

    @GetMapping("/jobapplications/{JobApplicationId}")
    public ResponseEntity<JobApplication> retrieve(@PathVariable(value = "JobApplicationId") int id) {
        return new ResponseEntity<>(this.jaService.getJobApplicationById(id), HttpStatus.OK);
    }

//    @GetMapping("/JobApplications/{JobApplicationId}/feedbacks")
//    public ResponseEntity<List<Feedback>> getFeedbacks(@PathVariable(value = "JobApplicationId") int id) {
//        return new ResponseEntity<>(this.jaService.getFeedbacks(id), HttpStatus.OK);                
//    }
    @PostMapping("/addapplication")
    public ResponseEntity<?> addApplication(
            @ModelAttribute("jobapplication") JobApplication jobApplication,
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {

        System.out.println("==== ĐÃ NHẬN ĐƠN ỨNG TUYỂN ====");
        System.out.println("Cover letter: " + jobApplication.getCoverLetter());

        if (jobApplication.getJobId() != null) {
            System.out.println("Job ID: " + jobApplication.getJobId().getId());
        } else {
            System.out.println("Job NULL ❌");
            return ResponseEntity.badRequest().body("Job ID không được để trống");
        }

        String username = authentication.getName();
        User user = userService.getUserByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Người dùng chưa đăng nhập");
        }

        jobApplication.setUserId(user);
        System.out.println("User ID: " + user.getId());

        // Xử lý file (resume)
        if (file != null && !file.isEmpty()) {
            try {
                // Giả sử JobApplication có phương thức setFile để lưu MultipartFile
                jobApplication.setFile(file);

                // Nếu bạn lưu file trên ổ đĩa hoặc dịch vụ file, thực hiện ở đây
                // Ví dụ:
                // String fileUrl = fileStorageService.storeFile(file);
                // jobApplication.setResumeLink(fileUrl);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Lỗi khi xử lý file đính kèm");
            }
        } else {
            System.out.println("Không có file đính kèm");
        }

        try {
            this.jaService.addOrUpdateJobApplication(jobApplication);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lưu đơn ứng tuyển");
        }

        return ResponseEntity.ok("Ứng tuyển thành công");
    }
}
