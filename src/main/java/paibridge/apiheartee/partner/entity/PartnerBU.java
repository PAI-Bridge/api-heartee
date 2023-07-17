package paibridge.apiheartee.partner.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import paibridge.apiheartee.member.entity.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("BU") //BU (Broke Up)
@Table(name = "partner_bu")
public class PartnerBU extends Partner{

    @Column(name = "info_bu")
    private String infoBU;

    @Builder
    public PartnerBU(Member member, String nickname, String gender, Integer age, Mbti mbti, String info) {
        super(member, nickname, gender, age, mbti);
        this.infoBU = info;
    }
}
