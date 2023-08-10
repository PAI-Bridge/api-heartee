package paibridge.apiheartee.conversation.service.image;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import com.google.rpc.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ImageTextExtractor {

    public ArrayList<List<EntityAnnotation>> extractTextListFromImage(MultipartFile image) throws IOException {
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

            ArrayList<List<EntityAnnotation>> responseList = responses.stream().map(res -> res.getTextAnnotationsList()).collect(Collectors.toCollection(ArrayList::new));

            return responseList;
        } catch (Exception e) {
            throw e;
        }

//
//            for (AnnotateImageResponse res : responses) {
//                System.out.println("RESPONSE LIST START");
//
//                if (res.hasError()) {
//                    Status error = res.getError();
//                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, error.getMessage());
//                }
//
//                List<EntityAnnotation> textAnnotationsList = res.getTextAnnotationsList();
//
//                String description = res.getTextAnnotationsList().get(0).getDescription(); // 첫 번째 description이 전체 텍스트를 하나의 string으로 묶은 값으로 보임
//                System.out.println("description = " + description);
//                System.out.println("FIRST ELEMENT END");
//
//                System.out.println("INNER LOOP START");
//
//                for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
//                    System.out.println("Text Begin");
//                    System.out.format("Text: %s\n", annotation.getDescription());
//                    System.out.println("TEXT END");
//                }
//                System.out.println("INNER LOOP END");
//
//                client.close();
//                // 전체 텍스트 String을 반환
//                return res.getTextAnnotationsList().get(0).getDescription();
//            }
//        } catch (Exception e) {
//
//            throw e;
//        }
//        return "";


    }
}