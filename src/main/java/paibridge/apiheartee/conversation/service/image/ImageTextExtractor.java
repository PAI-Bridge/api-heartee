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
                System.out.println("RESPONSE LIST START");

                if (res.hasError()) {
                    System.out.println("Error: " + res.getError().getMessage());
                    return "Error: " + res.getError().getMessage();
                }

                System.out.println("FIRST ELEMENT DESCRIPTION");
                String description = res.getTextAnnotationsList().get(0).getDescription(); // 첫 번째 description이 전체 텍스트를 하나의 string으로 묶은 값으로 보임
                System.out.println("description = " + description);
                System.out.println("FIRST ELEMENT END");

                System.out.println("INNER LOOP START");

                for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
                    System.out.println("Text Begin");
                    System.out.format("Text: %s\n", annotation.getDescription());
                    System.out.println("TEXT END");
                }
                System.out.println("INNER LOOP END");

                client.close();
                // 전체 텍스트 String을 반환
                return res.getTextAnnotationsList().get(0).getDescription();
            }
        }
        return "";

    }
}
