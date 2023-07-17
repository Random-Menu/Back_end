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

    //내가 추가하고 있는거 (메뉴 hashMap에 저장)
    @Override
    public String RandomMenu() {

        //입력받은 메뉴를 키값이랑 하나씩 저장할 map
        Map MenuMap = new HashMap<Integer, String>();

        String s = extract2(); //이미지 텍스트화한 걸 String 변수에 저장

        if(s.equals("/n")){   //줄바꿈 인식 될 때 마다 map에 메뉴 저장해야 됨
            for(int i=1; i<MenuMap.size(); i++){
                MenuMap.put(i, s);
            }
        }

        System.out.println(MenuMap);

        return s;
    }
}