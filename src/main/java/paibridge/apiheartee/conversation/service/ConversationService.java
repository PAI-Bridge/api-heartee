package paibridge.apiheartee.conversation.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ConversationService {

        public String extractTextAndGuessPrice(MultipartFile image) {
            return "extracted text";
        }
}
