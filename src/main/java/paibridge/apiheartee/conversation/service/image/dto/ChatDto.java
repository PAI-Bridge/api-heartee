package paibridge.apiheartee.conversation.service.image.dto;

import lombok.Builder;
import lombok.Data;
import paibridge.apiheartee.conversation.dto.ConversationDto;
import paibridge.apiheartee.conversation.entity.Conversation;

@Data
public class ChatDto {

    private Chatter chatter;
    private String chat;

    @Builder
    public ChatDto(Chatter chatter, String chat) {
        this.chatter = chatter;
        this.chat = chat;
    }

//    public static ChatDto create( entity) {
//        return ConversationDto.builder()
//                .sequence(entity.getSequence())
//                .imageUrl(entity.getImageUrl())
//                .build();
//    }

}
