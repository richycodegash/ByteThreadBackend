package com.jamboSwift.ByteThread.Services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public Map<String, String> uploadFile(MultipartFile file, String folderName) {
        try {
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", folderName)
            );

            String secureUrl=uploadResult.get("secure_url").toString();
            String publicId = uploadResult.get("public_id").toString();

            Map<String,String> urlMap=new HashMap<>();
            urlMap.put("secure_url", secureUrl);
            urlMap.put("public_id", publicId);
            return urlMap;
        } catch (IOException e) {
            throw new RuntimeException("Image upload failed: " + e.getMessage());
        }
    }

    public boolean deleteFileFromCloudinary(String publicId) {
        try {
            if (publicId == null || publicId.isEmpty()) {
                return false;
            }

            Map result = cloudinary.uploader().destroy(
                    publicId,
                    ObjectUtils.asMap("resource_type", "image")
            );

            // Cloudinary returns "result": "ok" if successful, or "not found" if it didn't exist
            String status = (String) result.get("result");
            return "ok".equals(status);

        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file from Cloudinary: " + e.getMessage());
        }
    }
}
