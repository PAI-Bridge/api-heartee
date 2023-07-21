package paibridge.apiheartee.conversation.service.image;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageUploader imageUploader;
    private final ImageTextExtractor imageTextExtractor;

    public String uploadImage(MultipartFile image) throws IOException {
        String uploadedUrl = this.imageUploader.uploadImage(image);

        return uploadedUrl;
    }

    public String extractTextFromImage(MultipartFile image) throws IOException{
        String extractedStr = this.imageTextExtractor.extractTextFromImage(image);

        System.out.println("extractedStr = " + extractedStr);
        return extractedStr;

        // TODO : 이미지들을 S3에 업로드 => 이미지에서 텍스트를 추출하여 반환
    }
}
