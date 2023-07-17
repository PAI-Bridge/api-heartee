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
@DiscriminatorValue("BU") //BU (Broke Up)
@Table(name = "counsel_report_bu")
public class CounselReportBU extends CounselReport {

    @Column(name = "stat_bu")
    private Integer statBU;

    @Builder
    public CounselReportBU(CounselRequest counselRequest, Integer temperature, String summary, String solution, Integer statA,
        Integer statB, Integer statBU) {
        super(counselRequest, temperature, summary, solution, statA, statB);
        this.statBU = statBU;
    }
}
