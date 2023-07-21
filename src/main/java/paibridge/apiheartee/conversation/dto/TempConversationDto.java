package paibridge.apiheartee.conversation.dto;

import lombok.Builder;
import lombok.Data;
import paibridge.apiheartee.conversation.entity.TempConversation;
import paibridge.apiheartee.counsel.entity.CategoryType;
import paibridge.apiheartee.counsel.entity.CounselCategory;


@Data
@Builder
public class TempConversationDto {
    private Integer price;
    private Long tempConversationId;

    public static TempConversationDto toDto(TempConversation entity) {
        return TempConversationDto.builder()
                .price(entity.getPrice())
                .tempConversationId(entity.getId())
                    .build();
        }
    }

