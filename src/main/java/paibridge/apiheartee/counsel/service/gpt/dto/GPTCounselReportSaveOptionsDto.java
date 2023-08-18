package paibridge.apiheartee.counsel.service.gpt.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class GPTCounselReportSaveOptionsDto {
    private String dType;

    @Builder
    public static GPTCounselReportSaveOptionsDto create(String dType) {
        return GPTCounselReportSaveOptionsDto.builder()
                .dType(dType)
                .build();
    }
}
