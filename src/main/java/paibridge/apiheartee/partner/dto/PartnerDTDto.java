package paibridge.apiheartee.partner.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import paibridge.apiheartee.counsel.dto.CounselCategoryDto;
import paibridge.apiheartee.partner.entity.Mbti;

@Data
@EqualsAndHashCode(callSuper = true)
public class PartnerDTDto extends PartnerDto {

    private String infoDT;

    public PartnerDTDto(Long id, String nickname, String gender, Integer age, Mbti mbti,
        String dtype, CounselCategoryDto categoryDto, String infoDT) {
        super(id, nickname, gender, age, mbti, dtype, categoryDto);
        this.infoDT = infoDT;
    }
}
