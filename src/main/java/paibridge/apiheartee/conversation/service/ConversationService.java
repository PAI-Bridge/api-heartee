package paibridge.apiheartee.conversation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import paibridge.apiheartee.common.dto.DataResponse;
import paibridge.apiheartee.conversation.dto.TempConversationDto;
import paibridge.apiheartee.conversation.entity.TempConversation;
import paibridge.apiheartee.conversation.repository.TempConversationRepository;
import paibridge.apiheartee.conversation.service.image.ImageService;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ConversationService {
    private final ImageService imageService;
    private final TempConversationRepository tempConversationRepository;

        public TempConversationDto extractTextAndGuessPrice(MultipartFile image) throws IOException {
            String uploadedImageUrl = this.imageService.uploadImage(image);

            String extractedStr = this.imageService.extractTextFromImage(image);

            Integer guessedPrice = this.guessPrice(extractedStr);

            TempConversation temp = this.tempConversationRepository.save(TempConversation.builder()
                    .price(guessedPrice)
                    .data(extractedStr)
                    .build()
            );

            return TempConversationDto.builder()
                    .price(guessedPrice)
                    .tempConversationId(temp.getId())
                    .build();

        }

        private Integer guessPrice(String extractedStr) {
            return extractedStr.length();
            // TODO : 추출된 텍스트를 가지고 가격을 추측
            // FIXME : 한국어인데.... 가격은 그냥 length 기준으로 잡으면 되나
        }
}
