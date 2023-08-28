package paibridge.apiheartee.partner.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import paibridge.apiheartee.counsel.entity.CategoryType.Values;
import paibridge.apiheartee.member.entity.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue(Values.DT) //DT (Dating)
@Table(name = "partner_dt")
public class PartnerDT extends Partner {

    @Column(name = "info_dt")
    private String infoDT;

    @Builder
    public PartnerDT(Member member, String nickname, String gender, Integer age, Mbti mbti,
        String infoDT) {
        super(member, nickname, gender, age, mbti, Values.DT);
        this.infoDT = infoDT;
    }
}
