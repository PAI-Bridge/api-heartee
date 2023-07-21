package paibridge.apiheartee.partner.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import paibridge.apiheartee.counsel.dto.CounselCategoryDto;
import paibridge.apiheartee.counsel.entity.CategoryType;
import paibridge.apiheartee.counsel.entity.CounselCategory;
import paibridge.apiheartee.partner.entity.Mbti;
import paibridge.apiheartee.partner.entity.Partner;
import paibridge.apiheartee.partner.entity.PartnerBU;
import paibridge.apiheartee.partner.entity.PartnerDT;
import paibridge.apiheartee.partner.entity.PartnerGL;

@Data
@NoArgsConstructor
@Slf4j
public abstract class PartnerDto {

    private Long id;
    private String nickname;
    private String gender;
    private Integer age;
    private Mbti mbti;
    private String dtype;
    private CounselCategoryDto category;

    public PartnerDto(Long id, String nickname, String gender, Integer age, Mbti mbti,
        String dtype, CounselCategoryDto categoryDto) {
        this.id = id;
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
        this.mbti = mbti;
        this.dtype = dtype;
        this.category = categoryDto;
    }

    public static PartnerDto toDto(Partner entity, List<CounselCategory> categories) {
        //Partner 엔티티의 dtype이 "GL"인 경우 엔티티를 상속 엔티티로 캐스팅하고, DTO로 변환
        if (entity.getDtype().equals("GL")) {
            PartnerGL casted = (PartnerGL) entity;
            CounselCategory category = categories.stream()
                .filter(c -> c.getCode().equals(CategoryType.GL))
                .findFirst().orElse(null);

            return new PartnerGLDto(casted.getId(), casted.getNickname(), casted.getGender(),
                casted.getAge(), casted.getMbti(), casted.getDtype(),
                CounselCategoryDto.toDto(category), casted.getInfoGL());
        }

        //Partner 엔티티의 dtype이 "DT"인 경우 엔티티를 상속 엔티티로 캐스팅하고, DTO로 변환
        if (entity.getDtype().equals("DT")) {
            PartnerDT casted = (PartnerDT) entity;
            CounselCategory category = categories.stream()
                .filter(c -> c.getCode().equals(CategoryType.DT))
                .findFirst().orElse(null);

            return new PartnerDTDto(casted.getId(), casted.getNickname(), casted.getGender(),
                casted.getAge(), casted.getMbti(), casted.getDtype(),
                CounselCategoryDto.toDto(category), casted.getInfoDT());
        }

        //Partner 엔티티의 dtype이 "BU"인 경우 엔티티를 상속 엔티티로 캐스팅하고, DTO로 변환
        if (entity.getDtype().equals("BU")) {
            PartnerBU casted = (PartnerBU) entity;
            CounselCategory category = categories.stream()
                .filter(c -> c.getCode().equals(CategoryType.BU))
                .findFirst().orElse(null);

            return new PartnerBUDto(casted.getId(), casted.getNickname(), casted.getGender(),
                casted.getAge(), casted.getMbti(), casted.getDtype(),
                CounselCategoryDto.toDto(category), casted.getInfoBU());
        }

        return null;
    }
}
