package com.example.randommenu.Vision;

import com.google.cloud.spring.vision.CloudVisionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;


@Service
public class VisionServiceImpl implements VisionService{

    @Autowired
    private CloudVisionTemplate cloudVisionTemplate;

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    public String extractTextFromImage(MultipartFile file) {

        String textFromImage = cloudVisionTemplate.
                extractTextFromImage(file.getResource());

        return textFromImage;
    }

    public String extract2() {
        String imageUrl = "https://cloud.google.com/vision/docs/images/sign_text.png";
        return this.cloudVisionTemplate.extractTextFromImage(this.resourceLoader.getResource(imageUrl));
    }

}