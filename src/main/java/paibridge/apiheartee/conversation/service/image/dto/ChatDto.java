package paibridge.apiheartee.conversation.service.image.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ChatDto {

    private Author author;
    private String content;
    private String time;

    @Builder
    public ChatDto(Author author, String content, String time) {
        this.author = author;
        this.content = content;
        this.time = time;
    }

}
