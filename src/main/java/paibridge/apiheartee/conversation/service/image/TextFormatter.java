package paibridge.apiheartee.conversation.service.image;

import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BoundingPoly;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Vertex;
import org.springframework.stereotype.Service;
import paibridge.apiheartee.conversation.service.image.dto.ChatDto;
import paibridge.apiheartee.conversation.service.image.dto.Chatter;
import paibridge.apiheartee.conversation.service.image.dto.RawChatWithVertexDto;
import paibridge.apiheartee.conversation.service.image.dto.SquareChatAreaDto;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TextFormatter {

    public ArrayList<ChatDto> formatAnnotationsToChats(List<AnnotateImageResponse> imageTextResponses) throws IOException {
        // 1. 텍스트들의 좌표 영역 추출
        List<RawChatWithVertexDto> rawChatsWithVertices = extractTextWithVertices(imageTextResponses);

        // 2. y좌표의 시작, 끝이 동일한 단어들을 한 줄로 고려하여 합함.
        ArrayList<RawChatWithVertexDto> rawChatWithVertexDtos = concatWordsOfSameLine(rawChatsWithVertices);

        // 첫 chat은 이미지의 모든 텍스트를 포함하고 있음.
        RawChatWithVertexDto fullChatsString = rawChatsWithVertices.get(0);
        Integer imageCenterXVertex = getCenterXVertexOfImage(fullChatsString);

        // 3. y좌표 순서대로 순서 정렬

        // 4. 중앙 X 좌표값으로 발화자를 분류
        List<ChatDto> chatDtos = clarifyChattersByXVertex(rawChatWithVertexDtos, imageCenterXVertex);

        return (ArrayList<ChatDto>) chatDtos;
    }

    private List<RawChatWithVertexDto> extractTextWithVertices(List<AnnotateImageResponse> imageTextResponses) {
        return imageTextResponses
                .stream().map(
                        imageTextResponse -> formatImageResponseToChatWithVertices(imageTextResponse)
                ).flatMap(List::stream).collect(Collectors.toList());
    }

    private List<RawChatWithVertexDto> formatImageResponseToChatWithVertices(AnnotateImageResponse response) {
        List<EntityAnnotation> textAnnotations = response.getTextAnnotationsList();

        List<RawChatWithVertexDto> rawChatsWithVertices = textAnnotations.stream().map(textAnnotation -> {
            BoundingPoly boundingPoly = textAnnotation.getBoundingPoly();
            List<Vertex> verticesList = boundingPoly.getVerticesList();

            SquareChatAreaDto squareAreaOfChat = getSquareAreaOfChat(verticesList);

            String description = textAnnotation.getDescription();

            RawChatWithVertexDto rawChatWithVertex = RawChatWithVertexDto.rawChatWithVertexDtoBuilder()
                    .chat(description)
                    .xVertexStart(squareAreaOfChat.getXVertexStart())
                    .xVertexEnd(squareAreaOfChat.getXVertexEnd())
                    .yVertexStart(squareAreaOfChat.getYVertexStart())
                    .yVertexEnd(squareAreaOfChat.getYVertexEnd())
                    .build();

            return rawChatWithVertex;
        }).collect(Collectors.toList());

        return rawChatsWithVertices;
    }

    private SquareChatAreaDto getSquareAreaOfChat(List<Vertex> vertices) {
        try {
            Integer xVertexStart = vertices.get(0).getX();
            Integer xVertexEnd = vertices.get(1).getX();
            Integer yVertexStart = vertices.get(0).getY();
            Integer yVertexEnd = vertices.get(2).getY();

            SquareChatAreaDto squareChatAreaDto = SquareChatAreaDto.builder()
                    .xVertexStart(xVertexStart)
                    .xVertexEnd(xVertexEnd)
                    .yVertexStart(yVertexStart)
                    .yVertexEnd(yVertexEnd)
                    .build();

            return squareChatAreaDto;
        } catch (Exception e) {
            return null;
        }
    }

    private Integer getCenterXVertexOfImage(RawChatWithVertexDto firstChatWithVertex) {
        Integer xVertexStart = firstChatWithVertex.getXVertexStart();
        Integer xVertexEnd = firstChatWithVertex.getXVertexEnd();


        Integer centerXVertex = (xVertexStart + xVertexEnd) / 2;

        return centerXVertex;
    }

    private List<ChatDto> clarifyChattersByXVertex(ArrayList<RawChatWithVertexDto> rawChatWithVertexDtos, Integer centerXVertex)  {
        ArrayList<RawChatWithVertexDto> rawChatsWithoutFullChat = removeFullChatStringFromChatsArr(rawChatWithVertexDtos);

         List<ChatDto> chatDtos = rawChatsWithoutFullChat.stream().map((chat) -> {
            Integer xVertexStart = chat.getXVertexStart();
            Integer xVertexEnd = chat.getXVertexEnd();

            Integer centerXVertexOfChat = (xVertexStart + xVertexEnd) / 2;

            if (centerXVertexOfChat < centerXVertex) return ChatDto.builder()
                    .chat(chat.getChat())
                    .chatter(Chatter.PARTNER)
                    .build();

            return ChatDto.builder()
                    .chat(chat.getChat())
                    .chatter(Chatter.USER)
                    .build();

            // 문장별로 발화자를 분류
        }).collect(Collectors.toList());

        return chatDtos;
    }

    private ArrayList<RawChatWithVertexDto> concatWordsOfSameLine(List<RawChatWithVertexDto> rawChatsWithVertices) {
        HashMap<String, RawChatWithVertexDto> wordsInSameLineConcat = new HashMap<String, RawChatWithVertexDto>();

        rawChatsWithVertices.stream().forEach(rawChatWithVertex -> {
            String chat = rawChatWithVertex.getChat();
            Integer yVertexStart = rawChatWithVertex.getYVertexStart();
            Integer yVertexEnd = rawChatWithVertex.getYVertexEnd();

            String key = yVertexStart.toString() + "-" + yVertexEnd.toString();

            if (wordsInSameLineConcat.containsKey(key)) {
                RawChatWithVertexDto rawChatWithVertexDto = wordsInSameLineConcat.get(key);
                String chatOfRawChatWithVertexDto = rawChatWithVertexDto.getChat();

                String concatChat = chatOfRawChatWithVertexDto + " " + chat;

                rawChatWithVertexDto.setChat(concatChat);
                rawChatWithVertexDto.setXVertexEnd(rawChatWithVertex.getXVertexEnd());
                
            } else {
                wordsInSameLineConcat.put(key, rawChatWithVertex);
            }
        });

        return new ArrayList<>(wordsInSameLineConcat.values());
    }
    private ArrayList<RawChatWithVertexDto> removeFullChatStringFromChatsArr(List<RawChatWithVertexDto> rawChatsWithVertices) {
        ArrayList<RawChatWithVertexDto> rawChatsWithVerticesClone = new ArrayList<>(rawChatsWithVertices);
        rawChatsWithVerticesClone.remove(0);

        return rawChatsWithVerticesClone;
    }
}

// FIXME : clarify 관련 초기 코드

//        for (String chat : allChatsSeparatedByLines) {
//            RawChatWithVertexDto rawChatWithVertex = rawChatsWithVertices.stream().filter(rawChat -> rawChat.getChat().equals(chat)).findFirst().get();
//
//            Integer xVertexStart = rawChatWithVertex.getXVertexStart();
//            Integer xVertexEnd = rawChatWithVertex.getXVertexEnd();
//
//            Integer centerXVertexOfChat = (xVertexStart + xVertexEnd) / 2;
//
//            if (centerXVertexOfChat < centerXVertex) {
//                ChatDto chatDto = ChatDto.builder()
//                        .chat(chat)
//                        .chatter("A")
//                        .build();
//
//                chatDtos.add(chatDto);
//            } else {
//                ChatDto chatDto = ChatDto.builder()
//                        .chat(chat)
//                        .chatter("B")
//                        .build();
//
//                chatDtos.add(chatDto);
//            }
//        }

//        return chatDtos;