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

import java.io.IOException;
import java.util.Map;


@RestController
public class VisionController {

    @Autowired
    public VisionController(VisionService visionService) {
        this.visionService = visionService;
    }

    @Autowired
    private VisionService visionService;
    @Autowired
    private VisionServiceImpl visionServiceImpl;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private CloudVisionTemplate cloudVisionTemplate;

    // 이미지 업로드 및 글자 추출


    // 이미지 업로드 및 추천 텍스트 추출
    @PostMapping("/uploadAndRecommend")
    public String uploadAndRecommend(@RequestParam("file") MultipartFile file) throws IOException {
        // 이미지를 업로드하고, 업로드한 이미지의 URL을 받아옴
        String imageUrl = visionService.uploadImage(file);

        // 이미지에서 추출된 텍스트를 가져옴
        String extractedText = visionService.extractTextFromImage(file, imageUrl);

        // 추출된 텍스트 맵에서 랜덤으로 하나의 텍스트 추출
        String randomText = visionService.getRandomExtractedText();

        // 업로드한 파일 삭제
        visionService.deleteImage(imageUrl);

        return "추출된 텍스트: " + extractedText + "\n추천 텍스트: " + randomText;
    }


    // 이미지 업로드
    @PostMapping("/uploadImage")
    public String uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        // 이미지를 업로드하고, 업로드한 이미지의 URL을 받아옴
        String imageUrl = visionService.uploadImage(file);
        return imageUrl;
    }


/**
 * 포스트맨에 사진파일을 업로드 -> 내가 지정해둔 경로에 저장됨(경로 즉 URL 반환) -> 리턴값으로 받은 URL로 텍스트를 추출함 -> 추출한 텍스트는 해쉬맵에 담겨짐
 */




//    //Extract the text in an image
//    @PostMapping("/extractTextFromImage")
//    public String extractTextFromImage(@RequestParam("file") MultipartFile file) {
//        // 이미지를 업로드하고, 업로드한 이미지의 URL을 받아옴
//        String imageUrl = visionService.uploadImage(file);
//
//        // VisionService의 extractTextFromImage 메서드에 imageUrl을 전달하여 텍스트를 추출
//        return visionService.extractTextFromImage(file, imageUrl);
//    }


}







