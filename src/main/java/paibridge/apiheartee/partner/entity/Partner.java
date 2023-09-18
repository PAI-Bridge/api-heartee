package paibridge.apiheartee.partner.entity;

import static javax.persistence.InheritanceType.JOINED;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import paibridge.apiheartee.common.entity.BaseEntity;
import paibridge.apiheartee.member.entity.Member;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = JOINED)
@DiscriminatorColumn(name = "dtype")
public abstract class Partner extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "partner_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String nickname;
    private String gender;
    private Integer age;

    @Enumerated(EnumType.STRING)
    private Mbti mbti;

    @Column(insertable = false, updatable = false) //자동 생성 컬럼인 dtype의 충돌 방지 + Getter 사용
    private String dtype;

    protected Partner(Member member, String nickname, String gender, Integer age, Mbti mbti) {
        this.member = member;
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
        this.mbti = mbti;
    }
}
