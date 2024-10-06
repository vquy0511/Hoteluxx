package com.hitori.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageUploadService {
    public String uploadImage(MultipartFile file) {
        String uploadDir = "E:/Hotelux/Hitori/src/main/resources/static/user/img/home-room/";

        // Tạo thư mục nếu chưa tồn tại
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try {
            // Lấy tên gốc của file
            String fileName = file.getOriginalFilename();

            // Tạo đường dẫn cho file mới
            Path path = Paths.get(uploadDir + fileName);

            // Lưu file vào đường dẫn đã tạo
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // Trả về đường dẫn của file sau khi đã lưu
            return "/user/img/home-room/" + fileName;
        } catch (IOException e) {
            // Xử lý exception nếu cần thiết
            e.printStackTrace();
            return null;
        }
    }
}
