package paibridge.apiheartee.counsel.service.gpt.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class GPTCounselRequestDto {

    private String language;
    private String userGender;
    private String counterpartGender;
    private String conversationHistory;

    @Builder
    public GPTCounselRequestDto(String language, String userGender, String counterpartGender, String conversationHistory) {
        this.language = language;
        this.userGender = userGender;
        this.counterpartGender = counterpartGender;
        this.conversationHistory = conversationHistory;
    }

    public static GPTCounselRequestDto create(String language, String userGender, String counterpartGender, String conversationHistory) {
        return GPTCounselRequestDto.builder()
            .language(language)
            .userGender(userGender)
            .counterpartGender(counterpartGender)
            .conversationHistory(conversationHistory)
            .build();
    }
}
