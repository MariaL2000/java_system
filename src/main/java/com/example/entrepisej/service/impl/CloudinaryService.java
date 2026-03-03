package com.example.entrepisej.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.entrepisej.service.ICloudinaryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService implements ICloudinaryService { 

    private final Cloudinary cloudinary;

    public CloudinaryService(
            @Value("${cloudinary.cloud_name}") String name,
            @Value("${cloudinary.api_key}") String key,
            @Value("${cloudinary.api_secret}") String secret) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", name,
            "api_key", key,
            "api_secret", secret
        ));
    }

    @Override // Indica que este método cumple con la interfaz
    public String uploadImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        // Subimos y retornamos la URL
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("url").toString();
    }
}