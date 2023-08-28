package paibridge.apiheartee.counsel.service.gpt.dto;

import lombok.Builder;
import lombok.Data;
import paibridge.apiheartee.conversation.service.image.dto.Author;

@Data
public class ChatForGPTRequestDto {

    private String author;
    private String content;
    private String time;

    @Builder
    public ChatForGPTRequestDto(String author, String content, String time) {
        this.author = author;
        this.content = content;
        this.time = time;
    }
}


