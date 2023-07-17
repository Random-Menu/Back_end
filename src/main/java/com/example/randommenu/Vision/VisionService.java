package com.example.randommenu.Vision;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface VisionService {

    String extractTextFromImage(MultipartFile file);

}
