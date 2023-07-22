package paibridge.apiheartee.conversation.dto;

import lombok.Builder;
import lombok.Data;
import paibridge.apiheartee.conversation.entity.TempConversation;


@Data
@Builder
public class TempConversationDto {

    private Integer price;
    private Long tempConversationId;

    public static TempConversationDto create(TempConversation entity) {
        return TempConversationDto.builder()
            .price(entity.getPrice())
            .tempConversationId(entity.getId())
            .build();
    }
}

