package paibridge.apiheartee.partner.dto;

import lombok.Data;
import paibridge.apiheartee.partner.entity.Mbti;

@Data
public class PartnerCreateDto {

    private String nickname;
    private String gender;
    private Integer age;
    private Mbti mbti;
    private String dtype;

    private String infoGL;
    private String infoDT;
    private String infoBU;
}
