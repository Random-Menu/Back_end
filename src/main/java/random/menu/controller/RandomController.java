package random.menu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import random.menu.service.RandomTestService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/menu")
public class RandomController {

    private final RandomTestService randomTestService;

    // List<String> 변수 선언
    private List<String> textList = new ArrayList<>();

    @Autowired
    public RandomController(RandomTestService randomTestService) {
        this.randomTestService = randomTestService;
    }

    @PostMapping("/addHtmlText")
    public String addHtmlTextToList(@RequestParam("htmlText") String randomText) {
        // List<String> 변수에 텍스트 추가
        textList.add(randomText);
        return "redirect:/menu/upload"; // 다시 입력 폼으로 리다이렉트
    }
}
