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
@DiscriminatorValue(Values.DT) //DT (Dating)
@Table(name = "counsel_report_dt")
public class CounselReportDT extends CounselReport {

    @Column(name = "stat_dt")
    private Integer statDT;

    @Builder
    public CounselReportDT(CounselRequest counselRequest, Integer temperature, String summary,
        String solution, Integer statA,
        Integer statB, Integer statDT) {
        super(counselRequest, temperature, summary, solution, statA, statB);
        this.statDT = statDT;
    }
}
