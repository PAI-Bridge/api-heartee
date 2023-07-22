package paibridge.apiheartee.conversation.dto;

import lombok.Builder;
import lombok.Data;
import paibridge.apiheartee.conversation.entity.Conversation;

@Data
public class ConversationDto {

    private Integer sequence;
    private String imageUrl;

    @Builder
    public ConversationDto(Integer sequence, String imageUrl) {
        this.sequence = sequence;
        this.imageUrl = imageUrl;
    }

    public static ConversationDto create(Conversation entity) {
        return ConversationDto.builder()
            .sequence(entity.getSequence())
            .imageUrl(entity.getImageUrl())
            .build();
    }
}
