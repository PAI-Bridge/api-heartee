package paibridge.apiheartee.partner.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import paibridge.apiheartee.counsel.dto.CounselCategoryDto;
import paibridge.apiheartee.partner.entity.Mbti;

@Data
@EqualsAndHashCode(callSuper = true)
public class PartnerGLDto extends PartnerDto {

    private String infoGL;

    public PartnerGLDto(Long id, String nickname, String gender, Integer age, Mbti mbti,
        String dtype, CounselCategoryDto categoryDto, String infoGL) {
        super(id, nickname, gender, age, mbti, dtype, categoryDto);
        this.infoGL = infoGL;
    }
}
