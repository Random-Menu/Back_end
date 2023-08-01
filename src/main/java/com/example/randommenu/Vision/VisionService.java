package com.example.randommenu.Vision;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface VisionService {
    String extractTextFromImage(MultipartFile file, String imageUrl);


    // 이미지 업로드
    String uploadImage(MultipartFile file) throws IOException;
    // 추출된 텍스트 맵에서 랜덤으로 하나의 텍스트 추출
    String getRandomExtractedText();
    void deleteImage(String imageUrl);

}
