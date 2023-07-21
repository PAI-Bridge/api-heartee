package paibridge.apiheartee.conversation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class ImageService {

    private final ImageTextExtractor imageTextExtractor;

    @Autowired
    public ImageService(ImageTextExtractor imageTextExtractor) {
        this.imageTextExtractor = imageTextExtractor;
    }

    public String processImage(MultipartFile image) throws IOException{
        String extractedStr = this.imageTextExtractor.extractTextFromImage(image.getInputStream());
        System.out.println("extractedStr = " + extractedStr);
        return extractedStr;

        // TODO : 이미지들을 S3에 업로드 => 이미지에서 텍스트를 추출하여 반환
    }
}
