package paibridge.apiheartee.counsel.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import paibridge.apiheartee.counsel.dto.CounselCategoryDto;
import paibridge.apiheartee.counsel.entity.CounselCategory;
import paibridge.apiheartee.counsel.repository.CounselCategoryRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CounselCategoryService {

    private final CounselCategoryRepository counselCategoryRepository;

    public List<CounselCategoryDto> findCounselCategories() {
        List<CounselCategory> categories = counselCategoryRepository.findAll();
        return categories.stream()
            .map(CounselCategoryDto::create)
            .collect(Collectors.toList());
    }
}
