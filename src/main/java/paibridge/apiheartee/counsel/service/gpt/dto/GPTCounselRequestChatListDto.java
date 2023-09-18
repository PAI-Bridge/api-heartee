package paibridge.apiheartee.counsel.service.gpt.dto;

import paibridge.apiheartee.conversation.service.image.dto.ConcattedChatWithVertexAndTime;

public class GPTCounselRequestChatListDto {

    private ConcattedChatWithVertexAndTime[] chatList;

    public GPTCounselRequestChatListDto(ConcattedChatWithVertexAndTime[] chatList) {
        this.chatList = chatList;
    }
}
