package paibridge.apiheartee.counsel.dto;

import lombok.Data;
import paibridge.apiheartee.partner.dto.PartnerCreateDto;

@Data
public class CounselPartnerCreateDto {

    private PartnerCreateDto partner;
    private Long tempConversationId;

    public PartnerCreateDto getPartnerCreateDto() {
        return this.partner;
    }
}

