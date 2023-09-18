package paibridge.apiheartee.conversation.service.image.dto;

import lombok.Builder;

public class ConcattedChatWithVertexAndTime extends RawChatWithVertexDto {
    private String sentTime;

    @Builder(builderMethodName = "concattedChatWithVertexAndTimeBuilder")
    public ConcattedChatWithVertexAndTime(Integer xVertexStart, Integer xVertexEnd, Integer yVertexStart, Integer yVertexEnd, String chat, String sentTime) {
        super(xVertexStart, xVertexEnd, yVertexStart, yVertexEnd, chat);
        this.sentTime = sentTime;
    }

    public String getSentTime() {
        return sentTime;
    }

    public void setSentTime(String sentTime) {
        this.sentTime = sentTime;
    }
}
