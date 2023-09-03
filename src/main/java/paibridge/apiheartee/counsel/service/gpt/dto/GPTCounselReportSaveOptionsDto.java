package paibridge.apiheartee.counsel.service.gpt.dto;

import lombok.Builder;
import lombok.Data;
import paibridge.apiheartee.counsel.entity.CounselRequest;

@Data
public class GPTCounselReportSaveOptionsDto {
    private String dType;

    private CounselRequest counselRequest;
    @Builder
    public GPTCounselReportSaveOptionsDto(String dType, CounselRequest counselRequest) {
        this.dType = dType;
        this.counselRequest = counselRequest;
    }

    public static GPTCounselReportSaveOptionsDto create(CounselRequest counselRequest, String dType) {
        return GPTCounselReportSaveOptionsDto.builder()
                .counselRequest(counselRequest)
                .dType(dType)
                .build();
    }
}
