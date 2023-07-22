package paibridge.apiheartee.partner.controller;

import java.util.List;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import paibridge.apiheartee.common.dto.DataArrayResponse;
import paibridge.apiheartee.common.dto.DataResponse;
import paibridge.apiheartee.partner.dto.PartnerDto;
import paibridge.apiheartee.partner.service.PartnerService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/partners")
public class PartnerController {

    private final PartnerService partnerService;

    @Value("${value.member-id.temp}")
    private Long memberId;  //인증 개발 이후 JWT 추출 값으로 대체 (temp)

    @GetMapping
    public DataArrayResponse<PartnerDto> findPartners(
        @RequestParam(required = false) String dtype) {

        List<PartnerDto> partners = partnerService.findPartners(memberId, dtype);

        return new DataArrayResponse<>(partners);
    }

    @GetMapping("/{partnerId}")
    public DataResponse<PartnerDto> findPartner(@PathVariable Long partnerId)
        throws EntityNotFoundException {
        try {

            PartnerDto partner = partnerService.findPartner(partnerId);
            return new DataResponse<>(partner);

        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }
}
