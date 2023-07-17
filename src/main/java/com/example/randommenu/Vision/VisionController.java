package com.example.randommenu.Vision;

import com.example.randommenu.Vision.VisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.ResourceLoader;
import com.google.cloud.spring.vision.CloudVisionTemplate;


@RestController
public class VisionController {

    @Autowired
    private VisionService visionService;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private CloudVisionTemplate cloudVisionTemplate;


    @GetMapping("/extractTextFromImage")
    public String extract() {
        String imageUrl = "https://cloud.google.com/vision/docs/images/sign_text.png";
        return this.cloudVisionTemplate.extractTextFromImage(this.resourceLoader.getResource(imageUrl));
    }

    //Extract the text in an image
    @PostMapping("/extractTextFromImage")
    public String extractTextFromImage(
            @RequestParam MultipartFile file) {
        return visionService.extractTextFromImage(file);
    }

}

