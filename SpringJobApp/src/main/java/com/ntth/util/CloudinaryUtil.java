/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ntth.util;

/**
 *
 * @author User
 */
import com.cloudinary.Cloudinary;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author LOQ
 */
@Component// Đánh dấu để Spring quản lý bean
public class CloudinaryUtil {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadToCloudinary(MultipartFile file) throws IOException {
         // Nếu file rỗng thì không làm gì cả, trả về null
        if (file == null || file.isEmpty()) {
            return null;
        }
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), new HashMap<>());
        return (String) uploadResult.get("secure_url");
    }
}