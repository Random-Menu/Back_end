package random.menu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class RandomTestServiceImpl implements RandomTestService {
    @Autowired
    private TestService testService;

    @Override
    public String TextFromImage(String imageUrl) throws Exception {
        return TextFromImage(imageUrl);
    }

    @Override
    public String getRandomText(String imageUrl) throws Exception {
        String randomText = TextFromImage(imageUrl);
        List<String> textList = new ArrayList<>();

        // 추출된 텍스트를 개행 문자로 분할하여 개별 라인들을 리스트에 추가합니다.
        String[] lines = randomText.split("\\r?\\n");
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                textList.add(line.trim());
            }
        }

        // 추출된 텍스트 중에서 랜덤하게 한 가지를 선택합니다.
        if (!textList.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(textList.size());
            return textList.get(randomIndex);
        } else {
            return "이미지에서 텍스트를 추출하지 못했습니다.";
        }
    }
}
