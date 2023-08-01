package com.example.randommenu.Vision;

import com.google.cloud.spring.vision.CloudVisionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@Service
public class VisionServiceImpl implements VisionService{

    // 이미지 업로드 경로 설정
    private String UPLOAD_DIR = "upload-dir";


    /**
     * extractedTextMap은 이미지의 URL과 추출된 텍스트를 저장하는데 사용되고, menuMap은 메뉴판 이미지의 인식된 메뉴 순서와 해당 메뉴의 이름을 저장하는데 사용
     */

    // 이미지에서 추출된 텍스트를 저장할 해시맵
    private final Map<String, String> extractedTextMap = new HashMap<>();


    // 메뉴판 이미지를 인식하여 저장할 해시맵 (숫자를 키로, 메뉴 이름을 값으로)
    private final Map<Integer, String> menuMap = new HashMap<>();
    private int menuCounter = 1; // 메뉴판 이미지의 순서를 저장하는 변수

    @Autowired
    private CloudVisionTemplate cloudVisionTemplate;
    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    public VisionServiceImpl(CloudVisionTemplate cloudVisionTemplate, ResourceLoader resourceLoader) {
        this.cloudVisionTemplate = cloudVisionTemplate;
        this.resourceLoader = resourceLoader;
    }


    @Override
    public String extractTextFromImage(MultipartFile file, String imageUrl) {
        String textFromImage = cloudVisionTemplate.
                extractTextFromImage(file.getResource());
        // 추출된 텍스트를 해시맵에 저장
        extractedTextMap.put(imageUrl, textFromImage);

        // 전체 해시맵 출력
        System.out.println("전체 해시맵 출력: " + extractedTextMap);

        // 메뉴판 이미지를 인식하여 해시맵에 저장
        String[] menuNames = textFromImage.split("\\n"); // 추출된 텍스트를 개행문자로 분리하여 메뉴 이름들을 배열에 저장
        for (String menuName : menuNames) {
            menuMap.put(menuCounter++, menuName);
        }
        return textFromImage;
    }



    public String uploadImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        // 업로드할 경로 설정
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // 중복 파일명 처리를 위해 랜덤한 고유 식별자를 파일명에 추가
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        String imageUrl = uploadPath.toString() + "/" + fileName;
        Path targetPath = uploadPath.resolve(fileName);
        try {
            Files.copy(file.getInputStream(), targetPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return imageUrl;
    }


    // 추출된 텍스트 맵에서 랜덤으로 하나의 텍스트 추출
    public String getRandomExtractedText() {
        if (extractedTextMap.isEmpty()) {
            return "추출된 텍스트가 없습니다.";
        }

        List<String> extractedTextList = new ArrayList<>(extractedTextMap.values());
        int randomIndex = (int) (Math.random() * extractedTextList.size());
        return extractedTextList.get(randomIndex);
    }

    // 업로드한 파일 삭제
    public void deleteImage(String imageUrl) {
        try {
            Path imagePath = Paths.get(imageUrl);
            Files.deleteIfExists(imagePath);
        } catch (IOException e) {
            throw new RuntimeException("파일 삭제 중 오류가 발생하였습니다.", e);
        }
    }


}