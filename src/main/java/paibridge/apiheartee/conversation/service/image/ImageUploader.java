package paibridge.apiheartee.conversation.service.image;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Convert;
import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
@Service
public class ImageUploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadImage(MultipartFile image) throws IOException {
    try {
        String originalFilename = image.getOriginalFilename();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(image.getContentType());
        metadata.setContentLength(image.getSize());

        PutObjectResult putObjectResult = amazonS3Client.putObject(bucket, originalFilename, image.getInputStream(), metadata);

        return amazonS3Client.getUrl(bucket, originalFilename).toString();
    } catch (Exception e) {
        throw new IOException("UPLOAD FAIL");
        }
    }
}
