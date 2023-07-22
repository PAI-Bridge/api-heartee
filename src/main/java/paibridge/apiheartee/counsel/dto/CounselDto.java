package paibridge.apiheartee.counsel.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import paibridge.apiheartee.counsel.entity.CounselReport;
import paibridge.apiheartee.counsel.entity.CounselRequest;

@Data
public class CounselDto {

    private Long id;
    private Long partnerId;
    private LocalDateTime createdAt;
    private Boolean isComplete;
    private CounselRequestDto request;
    private CounselReportDto report;

    @Builder
    public CounselDto(Long id, Long partnerId, LocalDateTime createdAt, Boolean isComplete,
        CounselRequestDto request, CounselReportDto report) {
        this.id = id;
        this.partnerId = partnerId;
        this.createdAt = createdAt;
        this.isComplete = isComplete;
        this.request = request;
        this.report = report;
    }

    public static CounselDto create(CounselRequest requestEntity, CounselReport reportEntity) {
        CounselRequestDto requestDto = CounselRequestDto.create(requestEntity);

        if (reportEntity == null) {
            return CounselDto.builder()
                .id(requestEntity.getId())
                .partnerId(requestEntity.getPartner().getId())
                .createdAt(requestEntity.getCreatedAt())
                .isComplete(false)
                .request(requestDto)
                .report(null)
                .build();
        }

        return CounselDto.builder()
            .id(requestEntity.getId())
            .partnerId(requestEntity.getPartner().getId())
            .createdAt(requestEntity.getCreatedAt())
            .isComplete(true)
            .request(requestDto)
            .report(CounselReportDto.create(reportEntity))
            .build();
    }
}
