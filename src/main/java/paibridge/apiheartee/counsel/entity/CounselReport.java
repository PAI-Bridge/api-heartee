package paibridge.apiheartee.counsel.entity;

import static javax.persistence.InheritanceType.JOINED;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import paibridge.apiheartee.common.entity.BaseEntity;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = JOINED)
@DiscriminatorColumn(name = "dtype")
public abstract class CounselReport extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "counsel_report_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counsel_request_id")
    private CounselRequest counselRequest;

    private Integer temperature;
    private String summary;
    private String solution;

    @Column(name = "stat_a")
    private Integer statA; //모든 유형 공통 수치, 컬럼명 미정

    @Column(name = "stat_b")
    private Integer statB; //   모든 유형 공통 수치, 컬럼명 미정

    public CounselReport(CounselRequest counselRequest, Integer temperature, String summary, String solution, Integer statA,
        Integer statB) {
        this.counselRequest = counselRequest;
        this.temperature = temperature;
        this.summary = summary;
        this.solution = solution;
        this.statA = statA;
        this.statB = statB;
    }
}
