package paibridge.apiheartee.conversation.service.image;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageTextExtractor {

    public String extractTextFromImage(MultipartFile image) throws IOException {
        InputStream inputStream = image.getInputStream();

        ArrayList<AnnotateImageRequest> requests = new ArrayList<>();

        ByteString imgBytes = ByteString.readFrom(inputStream);

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feature = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();

        AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feature).setImage(img).build();

        requests.add(request);
        System.out.println("request = " + request);
        System.out.println("image = " + img);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.println("Error: " + res.getError().getMessage());
                    return "Error: " + res.getError().getMessage();
                }

                for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
                    System.out.format("Text: %s\n", annotation.getDescription());
                    System.out.format("Position : %s\n", annotation.getBoundingPoly());
                    return annotation.getDescription();

                }
                client.close();
            }
        }
        return "";

    }
}
