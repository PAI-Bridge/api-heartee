package paibridge.apiheartee.conversation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import paibridge.apiheartee.common.dto.DataResponse;
import paibridge.apiheartee.conversation.dto.TempConversationDto;
import paibridge.apiheartee.conversation.entity.TempConversation;
import paibridge.apiheartee.conversation.repository.TempConversationRepository;
import paibridge.apiheartee.conversation.service.image.ImageService;
import paibridge.apiheartee.conversation.service.image.dto.ChatDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class ConversationService {
    private final ImageService imageService;
    private final TempConversationRepository tempConversationRepository;

        public TempConversationDto extractTextAndGuessPrice(MultipartFile image) throws IOException {
            String uploadedImageUrl = imageService.uploadImage(image);

            ArrayList<ChatDto> chatDtos = imageService.extractChatsFromImage(image);

            Integer guessedPrice = guessPrice(chatDtos);

            String allChatsToString = chatDtos.stream().map(chatDto -> chatDto.getChatter().toString() + chatDto.getChat() + "\n").reduce("", (total, chat) -> total + chat);

            TempConversation temp = tempConversationRepository.save(TempConversation.builder()
                    .price(guessedPrice)
                    .data(allChatsToString)
                    .build()
            );

            return TempConversationDto.builder()
                    .price(guessedPrice)
                    .tempConversationId(temp.getId())
                    .build();

        }

        private Integer guessPrice(ArrayList<ChatDto> chatDtos) {
            // FIXME: 현재는 단순 길이로 가격 추정 중. 필요시 수정
            Integer totalLength = chatDtos.stream()
                    .map(chat -> chat.getChat().length())
                    .reduce(0, (acc, cur) -> acc + cur);

            return totalLength;
        }



}
