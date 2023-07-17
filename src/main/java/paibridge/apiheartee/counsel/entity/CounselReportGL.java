package paibridge.apiheartee.counsel.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("GL") //GL (Green Light)
@Table(name = "counsel_report_gl")
public class CounselReportGL extends CounselReport {

    @Column(name = "stat_gl")
    private Integer statGL;

    @Builder
    public CounselReportGL(CounselRequest counselRequest, Integer temperature, String summary, String solution, Integer statA,
        Integer statB, Integer statGL) {
        super(counselRequest, temperature, summary, solution, statA, statB);
        this.statGL = statGL;
    }
}
