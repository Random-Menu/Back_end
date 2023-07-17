package random.menu.service;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class TestServiceImpl implements TestService {

    private static final Logger logger = LoggerFactory.getLogger(TestServiceImpl.class);

    private static final String DIR_PATH = "\"https://upload.wikimedia.org/wikipedia/commons/thumb/b/b5/Lion_d%27Afrique.jpg/290px-Lion_d%27Afrique.jpg\"";

    public String saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String originalName = StringUtils.cleanPath(file.getOriginalFilename());
        String uuid = UUID.randomUUID().toString();
        String extension = getFileExtension(originalName);
        String savedName = uuid + extension;
        String savedPath = DIR_PATH + savedName;

        try {
            Path targetLocation = Path.of(savedPath);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.error("Failed to save file", e);
            throw new IOException("Failed to save file", e);
        }

        return savedPath;
    }

    public String extractKeywords(String imgFilePath) throws Exception {
        AtomicReference<String> labels = new AtomicReference<>("");

        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {
            byte[] data = Files.readAllBytes(Path.of(imgFilePath));
            ByteString imgBytes = ByteString.copyFrom(data);

            List<AnnotateImageRequest> requests = new ArrayList<>();
            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feat = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
            AnnotateImageRequest request =
                    AnnotateImageRequest.newBuilder()
                            .addFeatures(feat)
                            .setImage(img)
                            .build();
            requests.add(request);

            BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    logger.error("Error occurred during image annotation: {}", res.getError().getMessage());
                    return null;
                }

                List<EntityAnnotation> keywords = res.getLabelAnnotationsList();
                for (EntityAnnotation annotation : keywords) {
                    String description = annotation.getDescription();
                    labels.set(labels + description + "\n");
                }
            }
        }

        return labels.toString();
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
