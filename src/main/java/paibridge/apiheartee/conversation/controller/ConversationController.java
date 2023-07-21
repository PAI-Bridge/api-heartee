package paibridge.apiheartee.conversation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import paibridge.apiheartee.conversation.service.ConversationService;
import paibridge.apiheartee.conversation.service.ImageService;

import java.io.IOException;

@RestController
@RequestMapping("conversations")
public class ConversationController {
    private final ConversationService conversationService;

    @Autowired
    public ConversationController (ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @PostMapping(value="/text", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String extractTextAndGuessPrice(@RequestParam(value="image") MultipartFile image) throws IOException {
        return this.conversationService.extractTextAndGuessPrice(image);
    }

}
