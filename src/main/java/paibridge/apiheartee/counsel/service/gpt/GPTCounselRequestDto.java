package paibridge.apiheartee.counsel.service.gpt;

import lombok.Builder;
import lombok.Data;

@Data
public class GPTCounselRequestDto {

    private String language;
    private String userGender;
    private String counterpartGender;
    private String conversationHistory;

    @Builder
    public static GPTCounselRequestDto create(String language, String userGender, String counterpartGender, String conversationHistory) {
        return GPTCounselRequestDto.builder()
            .language(language)
            .userGender(userGender)
            .counterpartGender(counterpartGender)
            .conversationHistory(conversationHistory)
            .build();
    }
}
