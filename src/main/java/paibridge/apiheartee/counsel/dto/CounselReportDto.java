package paibridge.apiheartee.counsel.dto;

import lombok.Builder;
import lombok.Data;
import paibridge.apiheartee.counsel.entity.CategoryType.Values;
import paibridge.apiheartee.counsel.entity.CounselReport;
import paibridge.apiheartee.counsel.entity.CounselReportBU;
import paibridge.apiheartee.counsel.entity.CounselReportDT;
import paibridge.apiheartee.counsel.entity.CounselReportGL;

@Data
public class CounselReportDto {

    private String summary;
    private String solution;

    private Double willingness;
    private Double selfOpenness;
    private Double voiceOver;
    private Double positiveLanguage;
    private Double frequency;
    private String explanation;



    @Builder
    public CounselReportDto(String summary, String solution, Double willingness, Double selfOpenness, Double voiceOver, Double positiveLanguage, Double frequency, String explanation) {
        this.summary = summary;
        this.solution = solution;
        this.willingness = willingness;
        this.selfOpenness = selfOpenness;
        this.voiceOver = voiceOver;
        this.positiveLanguage = positiveLanguage;
        this.frequency = frequency;
        this.explanation = explanation;
    }

    public static CounselReportDto create(CounselReport entity) {
        CounselReportDto dto = CounselReportDto.builder()
            .summary(entity.getSummary())
            .solution(entity.getSolution())
                .willingness(entity.getWillingness())
                .selfOpenness(entity.getSelfOpenness())
                .voiceOver(entity.getVoiceOver())
                .positiveLanguage(entity.getPositiveLanguage())
                .frequency(entity.getFrequency())
                .explanation(entity.getExplanation())
            .build();
//
//        if (entity.getDtype().equals(Values.GL)) {
//            dto.setStatGL(((CounselReportGL) entity).getStatGL());
//        }
//
//        if (entity.getDtype().equals(Values.DT)) {
//            dto.setStatDT(((CounselReportDT) entity).getStatDT());
//        }
//
//        if (entity.getDtype().equals(Values.BU)) {
//            dto.setStatBU(((CounselReportBU) entity).getStatBU());
//        }

        return dto;
    }
}
