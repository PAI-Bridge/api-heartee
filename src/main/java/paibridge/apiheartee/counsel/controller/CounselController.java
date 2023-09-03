package paibridge.apiheartee.counsel.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import paibridge.apiheartee.common.dto.DataArrayResponse;
import paibridge.apiheartee.common.dto.DataResponse;
import paibridge.apiheartee.counsel.dto.CounselCreateDto;
import paibridge.apiheartee.counsel.dto.CounselDto;
import paibridge.apiheartee.counsel.dto.CounselPartnerCreateDto;
import paibridge.apiheartee.counsel.service.CounselService;
import paibridge.apiheartee.member.service.MemberService;

@Slf4j
@RestController
@RequestMapping("/counsels")
@RequiredArgsConstructor
public class CounselController {

    private final CounselService counselService;
    private final MemberService memberService;

    @GetMapping
    public DataArrayResponse<CounselDto> findCounsels(
        @RequestParam(required = false) Long partnerId,
        @RequestParam(required = false) Integer offset,
        @RequestParam(required = false) Integer limit
    ) {
        List<CounselDto> counselDtos = counselService.findCounsels(partnerId, offset, limit);

        return new DataArrayResponse<>(counselDtos);
    }

    @GetMapping("/{counselId}")
    public DataResponse<CounselDto> findCounsel(@PathVariable Long counselId)
        throws EntityNotFoundException {
        try {
            CounselDto counselDto = counselService.findCounsel(counselId);

            return new DataResponse<>(counselDto);

        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @PostMapping("/new")
    @ResponseStatus(code = HttpStatus.CREATED)
    public DataResponse<Map<String, Long>> createCounselAndPartner(
        @RequestBody CounselPartnerCreateDto dto, HttpServletRequest request)
        throws EntityNotFoundException {
        try {
            String oauthId = (String) request.getAttribute("oauthId");

            Long memberId = memberService.findMemberIdByOauthId(oauthId);
            Long counselId = counselService.createCounselAndPartner(memberId, dto);

            HashMap<String, Long> data = new HashMap<>();
            data.put("counselId", counselId);

            return new DataResponse<>(data);

        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @PostMapping("/add")
    @ResponseStatus(code = HttpStatus.CREATED)
    public DataResponse<Map<String, Long>> createCounsel(@RequestBody CounselCreateDto dto,
        HttpServletRequest request)
        throws EntityNotFoundException {
        try {
            String oauthId = (String) request.getAttribute("oauthId");

            Long memberId = memberService.findMemberIdByOauthId(oauthId);
            Long counselId = counselService.createCounsel(memberId, dto);

            HashMap<String, Long> data = new HashMap<>();
            data.put("counselId", counselId);

            return new DataResponse<>(data);

        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }
}
