package paibridge.apiheartee.counsel.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import paibridge.apiheartee.common.dto.DataArrayResponse;
import paibridge.apiheartee.counsel.dto.CounselCategoryDto;
import paibridge.apiheartee.counsel.service.CounselCategoryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/counsel-categories")
public class CounselCategoryController {

    private final CounselCategoryService counselCategoryService;

    @GetMapping
    public DataArrayResponse<CounselCategoryDto> findCounselCategories() {
        List<CounselCategoryDto> categories = counselCategoryService.findCounselCategories();

        return new DataArrayResponse<>(categories);
    }
}
