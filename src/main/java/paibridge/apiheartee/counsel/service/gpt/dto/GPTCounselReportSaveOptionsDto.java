package paibridge.apiheartee.counsel.service.gpt.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class GPTCounselReportSaveOptionsDto {
    private String dType;

    @Builder
    public GPTCounselReportSaveOptionsDto(String dType) {
        this.dType = dType;
    }

    public static GPTCounselReportSaveOptionsDto create(String dType) {
        return GPTCounselReportSaveOptionsDto.builder()
                .dType(dType)
                .build();
    }
}
