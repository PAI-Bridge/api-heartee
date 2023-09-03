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
@DiscriminatorValue(Values.GL) //GL (Green Light)
@Table(name = "counsel_report_gl")
public class CounselReportGL extends CounselReport {

    @Builder
    public CounselReportGL(CounselRequest counselRequest, String summary,
        String solution, Integer willingness, Integer selfOpenness, Integer voiceOver, Integer positiveLanguage, Integer frequency, String explanation) {
        super(counselRequest, summary, solution, willingness, selfOpenness, voiceOver, positiveLanguage, frequency, explanation);
    }
}
