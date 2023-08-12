package paibridge.apiheartee.conversation.service.image.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

public class RawChatWithVertexDto extends SquareChatAreaDto {


    private String chat;


    @Builder(builderMethodName = "rawChatWithVertexDtoBuilder")
    public RawChatWithVertexDto(Integer xVertexStart, Integer xVertexEnd, Integer yVertexStart, Integer yVertexEnd, String chat) {
        super(xVertexStart, xVertexEnd, yVertexStart, yVertexEnd);
        this.chat = chat;
    }

    public String getChat() {
        return chat;
    }
}


