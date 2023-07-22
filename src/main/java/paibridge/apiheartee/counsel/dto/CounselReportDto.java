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

    private Integer temperature;
    private Integer statA;
    private Integer statB;

    private Integer statGL;
    private Integer statDT;
    private Integer statBU;

    private String summary;
    private String solution;

    @Builder
    public CounselReportDto(Integer temperature, Integer statA, Integer statB, Integer statGL,
        Integer statDT, Integer statBU, String summary, String solution) {
        this.temperature = temperature;
        this.statA = statA;
        this.statB = statB;
        this.statGL = statGL;
        this.statDT = statDT;
        this.statBU = statBU;
        this.summary = summary;
        this.solution = solution;
    }

    public static CounselReportDto create(CounselReport entity) {
        CounselReportDto dto = CounselReportDto.builder()
            .temperature(entity.getTemperature())
            .statA(entity.getStatA())
            .statB(entity.getStatB())
            .summary(entity.getSummary())
            .solution(entity.getSolution())
            .build();

        if (entity.getDtype().equals(Values.GL)) {
            dto.setStatGL(((CounselReportGL) entity).getStatGL());
        }

        if (entity.getDtype().equals(Values.DT)) {
            dto.setStatDT(((CounselReportDT) entity).getStatDT());
        }

        if (entity.getDtype().equals(Values.BU)) {
            dto.setStatBU(((CounselReportBU) entity).getStatBU());
        }

        return dto;
    }
}
