package random.menu.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface TestService {
    String saveFile(MultipartFile file) throws IOException;
    String removeNumbersFromText(String text);
}
