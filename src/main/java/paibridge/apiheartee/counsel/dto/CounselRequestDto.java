package paibridge.apiheartee.counsel.dto;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import paibridge.apiheartee.conversation.dto.ConversationDto;
import paibridge.apiheartee.counsel.entity.CounselRequest;

@Data
public class CounselRequestDto {

    private String comment;
    private List<ConversationDto> conversations;
    private Long price;

    @Builder
    public CounselRequestDto(String comment, List<ConversationDto> conversations, Long price) {
        this.comment = comment;
        this.conversations = conversations;
        this.price = price;
    }

    public static CounselRequestDto create(CounselRequest entity) {
        List<ConversationDto> conversationDtos = entity.getConversations().stream()
            .map(ConversationDto::create).collect(
                Collectors.toList());

        return CounselRequestDto.builder()
            .comment(entity.getComment())
            .conversations(conversationDtos)
            .price(entity.getPrice())
            .build();
    }
}
