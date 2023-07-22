package paibridge.apiheartee.counsel.dto;

import lombok.Builder;
import lombok.Data;
import paibridge.apiheartee.counsel.entity.CategoryType;
import paibridge.apiheartee.counsel.entity.CounselCategory;

@Data
@Builder
public class CounselCategoryDto {

    private Long id;
    private CategoryType code;
    private String title;
    private String subtitle;
    private String imageUrl;

    public static CounselCategoryDto create(CounselCategory entity) {
        return CounselCategoryDto.builder()
            .id(entity.getId())
            .code(entity.getCode())
            .title(entity.getTitle())
            .subtitle(entity.getSubtitle())
            .imageUrl(entity.getImageUrl())
            .build();
    }
}
