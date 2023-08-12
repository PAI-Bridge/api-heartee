package paibridge.apiheartee.conversation.service.gpt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import paibridge.apiheartee.conversation.repository.TempConversationRepository;
import paibridge.apiheartee.conversation.service.image.dto.ChatDto;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class GPTService {
    private final TempConversationRepository tempConversationRepository;

    public String formatChatsToGPTRequest(ArrayList<ChatDto> chats) {
        return chats.stream().map(chatDto -> chatDto.getChatter().toString() + chatDto.getChat() + "\n").reduce("", (total, chat) -> total + chat);
    }
}
