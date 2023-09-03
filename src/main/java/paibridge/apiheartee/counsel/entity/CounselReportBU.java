package paibridge.apiheartee.counsel.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import paibridge.apiheartee.counsel.entity.CategoryType.Values;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue(Values.BU) //BU (Broke Up)
@Table(name = "counsel_report_bu")
public class CounselReportBU extends CounselReport {

    @Builder
    public CounselReportBU(CounselRequest counselRequest, String summary,
        String solution, Integer willingness, Integer selfOpenness, Integer voiceOver, Integer positiveLanguage, Integer frequency, String explanation) {
        super(counselRequest, summary, solution, willingness, selfOpenness, voiceOver, positiveLanguage, frequency, explanation);
//        this.statBU = statBU;
    }
}
