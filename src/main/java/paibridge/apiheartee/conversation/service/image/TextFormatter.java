package paibridge.apiheartee.conversation.service.image;

import com.google.cloud.vision.v1.EntityAnnotation;
import org.springframework.stereotype.Service;
import paibridge.apiheartee.conversation.service.image.dto.ChatDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TextFormatter {

    public ArrayList<ChatDto> formatAnnotationsToChat(ArrayList<List<EntityAnnotation>> textAnnotations) throws IOException {
        System.out.println("textAnnotations = " + textAnnotations);

    }
}
