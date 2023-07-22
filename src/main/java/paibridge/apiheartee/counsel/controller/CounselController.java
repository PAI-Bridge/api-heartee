package paibridge.apiheartee.counsel.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

@Slf4j
@RestController
@RequestMapping("/counsels")
@RequiredArgsConstructor
public class CounselController {

    private final CounselService counselService;

    @Value("${value.member-id.temp}")
    private Long memberId;  //인증 개발 이후 JWT 추출 값으로 대체 (temp)

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
        @RequestBody CounselPartnerCreateDto dto) throws EntityNotFoundException {
        try {
            Long counselId = counselService.createCounselAndPartner(memberId, dto);

            HashMap<String, Long> data = new HashMap<>();
            data.put("counselId", counselId);

            return new DataResponse<>(data);

        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @PostMapping("/not-new")
    @ResponseStatus(code = HttpStatus.CREATED)
    public DataResponse<Map<String, Long>> createCounsel(@RequestBody CounselCreateDto dto)
        throws EntityNotFoundException {
        try {
            Long counselId = counselService.createCounsel(memberId, dto);

            HashMap<String, Long> data = new HashMap<>();
            data.put("counselId", counselId);

            return new DataResponse<>(data);

        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }
}
