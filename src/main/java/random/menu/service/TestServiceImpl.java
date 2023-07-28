package random.menu.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TestServiceImpl implements TestService {

    private static final Logger logger = LoggerFactory.getLogger(TestServiceImpl.class);

    @Override
    public String saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String originalName = StringUtils.cleanPath(file.getOriginalFilename());
        String uuid = UUID.randomUUID().toString();
        String extension = getFileExtension(originalName);
        String savedName = uuid + extension;
        String savedPath  = "C:/path/to/your/upload" + savedName;   // 실제 시스템의 디렉토리 경로에 맞게 디렉토리 경로를 업데이트

        try {
            Path targetLocation = Path.of(savedPath);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.error("파일 저장에 실패했습니다", e);
            throw new IOException("파일 저장에 실패했습니다", e);
        }

        return savedPath;
    }

    @Override
    public String removeNumbersFromText(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        String regex = "\\d+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        return matcher.replaceAll("");
    }

    private String getFileExtension(String filename) {
        if (StringUtils.hasLength(filename)) {
            int dotIndex = filename.lastIndexOf(".");
            if (dotIndex > 0 && dotIndex < filename.length() - 1) {
                return filename.substring(dotIndex);
            }
        }
        return "";
    }
}
