package paibridge.apiheartee.conversation.service.image.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;


@Data
public class SquareChatAreaDto {
    protected Integer xVertexStart;
    protected Integer xVertexEnd;
    protected Integer yVertexStart;

    protected Integer yVertexEnd;

    @Builder
    public SquareChatAreaDto(Integer xVertexStart, Integer xVertexEnd, Integer yVertexStart, Integer yVertexEnd) {
        this.xVertexStart = xVertexStart;
        this.xVertexEnd = xVertexEnd;
        this.yVertexStart = yVertexStart;
        this.yVertexEnd = yVertexEnd;
    }
}