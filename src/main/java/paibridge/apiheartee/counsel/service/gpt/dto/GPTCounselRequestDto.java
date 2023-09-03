package paibridge.apiheartee.counsel.service.gpt.dto;

import lombok.Builder;
import lombok.Data;
import paibridge.apiheartee.counsel.entity.CounselRequest;

@Data
public class GPTCounselRequestDto {

    private CounselRequest counselRequest;
    private String language;
    private String user_gender;
    private String counterpart_gender;
    private String conversation_history;

    @Builder
    public GPTCounselRequestDto(CounselRequest counselRequest, String language, String user_gender, String counterpart_gender, String conversation_history) {
        this.counselRequest = counselRequest;
        this.language = language;
        this.user_gender = user_gender;
        this.counterpart_gender = counterpart_gender;
        this.conversation_history = conversation_history;
    }

    public static GPTCounselRequestDto create(CounselRequest counselRequest, String language, String userGender, String counterpartGender, String conversationHistory) {
        return GPTCounselRequestDto.builder()
                .counselRequest(counselRequest)
            .language(language)
                .user_gender(userGender)
            .counterpart_gender(counterpartGender)
            .conversation_history(conversationHistory)
            .build();
    }
}
