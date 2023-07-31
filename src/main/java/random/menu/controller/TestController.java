package random.menu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import random.menu.service.TestService;

import java.io.IOException;

@Controller
@RequestMapping("/menu")
public class TestController {

    private final TestService testService;

    @Autowired
    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/upload")
    public String showUploadForm() {
        return "uploadFile"; // uploadFile.html 템플릿을 반환
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        try {
            String savedPath = testService.saveFile(file); // 파일을 저장하고 저장된 경로를 받아옴
            String visionText = extractTextFromImage(savedPath); // Vision API를 사용하여 텍스트 추출
            String processedText = testService.removeNumbersFromText(visionText);   // 텍스트에서 숫자를 제거하여 처리된 텍스트를 얻음

            model.addAttribute("originalText", visionText);
            model.addAttribute("processedText", processedText);

            return "result"; // result.html 템플릿을 반환
        } catch (IOException e) {
            // 파일 업로드 또는 처리 과정에서 예외가 발생한 경우 에러 페이지로 이동
            return "error"; // error.html 템플릿을 반환
        }
    }

    // Vision API로 이미지 파일을 읽고 텍스트를 추출하는 메서드라고 가정
    private String extractTextFromImage(String processedText) {
        // Vision API 사용 로직 구현
        return processedText;
    }
}
