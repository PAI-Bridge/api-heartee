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
@DiscriminatorValue(Values.GL) // GL (Green Light)
@Table(name = "partner_gl")
public class PartnerGL extends Partner {

    @Column(name = "info_gl")
    private String infoGL;

    @Builder
    public PartnerGL(Member member, String nickname, String gender, Integer age, Mbti mbti,
        String infoGL) {
        super(member, nickname, gender, age, mbti);
        this.infoGL = infoGL;
    }
}
