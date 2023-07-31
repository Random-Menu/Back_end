package random.menu.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TestServiceImpl implements TestService {

    private static final Logger logger = LoggerFactory.getLogger(TestServiceImpl.class);
    private static final String UPLOAD_DIRECTORY = "uploads"; // 업로드된 파일을 저장할 디렉토리 이름

    @Override
    public String saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String originalName = StringUtils.cleanPath(file.getOriginalFilename());
        String uuid = UUID.randomUUID().toString();
        String extension = getFileExtension(originalName);
        String savedName = uuid + extension;

        // 현재 작업 디렉토리에 /uploads 디렉토리 생성
        Path uploadPath = Paths.get(System.getProperty("user.dir"), UPLOAD_DIRECTORY);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String savedPath = uploadPath.resolve(savedName).toString();

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
