package paibridge.apiheartee.partner.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import paibridge.apiheartee.common.dto.DataArrayResponse;
import paibridge.apiheartee.common.dto.DataResponse;
import paibridge.apiheartee.counsel.service.CounselCategoryService;
import paibridge.apiheartee.partner.dto.PartnerDto;
import paibridge.apiheartee.partner.service.PartnerService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/partners")
public class PartnerController {

    private final PartnerService partnerService;
    private final CounselCategoryService counselCategoryService;

    @GetMapping
    public DataArrayResponse<PartnerDto> findPartners(@RequestParam String dtype) {

        Long memberId = 1L; //인증 개발 이후 JWT 추출 값으로 대체
        log.info("dtype = {}", dtype);

        List<PartnerDto> partners = partnerService.findPartners(memberId, dtype);

        return new DataArrayResponse<>(partners);
    }

    @GetMapping("/{partnerId}")
    public DataResponse<PartnerDto> findPartner(@PathVariable Long partnerId) {

        PartnerDto partner = partnerService.findPartner(partnerId);

        if (partner == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Partner not found");
        }

        return new DataResponse<>(partner);
    }
}
