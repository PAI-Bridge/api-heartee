package paibridge.apiheartee.conversation.service.image;

import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BoundingPoly;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Vertex;
import org.springframework.stereotype.Service;
import paibridge.apiheartee.conversation.service.image.dto.ChatDto;
import paibridge.apiheartee.conversation.service.image.dto.RawChatWithVertexDto;
import paibridge.apiheartee.conversation.service.image.dto.SquareChatAreaDto;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TextFormatter {

    public ArrayList<ChatDto> formatAnnotationsToChats(List<AnnotateImageResponse> imageTextResponses) throws IOException {
        // 1. 텍스트들의 좌표 영역 추출
        List<RawChatWithVertexDto> rawChatsWithVertices = extractTextWithVertices(imageTextResponses);

        // 첫 chat은 이미지의 모든 텍스트를 포함하고 있음.
        RawChatWithVertexDto fullChatsString = rawChatsWithVertices.get(0);

        System.out.println("fullChatsString = " + fullChatsString.getChat());

        // FIXME : 단어들에 대해 loop를 돌면서 y좌표의 시작과 끝이 동일한 것들을 붙여나간다.

        String[] allChatsSepartedByLines = fullChatsString.getChat().split("\n");
        System.out.println("allChatsSepartedByLines = " + allChatsSepartedByLines.length);
        Integer imageCenterXVertex = getCenterXVertexOfImage(fullChatsString);

        // 2. 중앙 X 좌표값으로 발화자를 분류
        List<ChatDto> chatDtos = clarifyChattersByXVertex(allChatsSepartedByLines, rawChatsWithVertices, imageCenterXVertex);

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

    private List<ChatDto> clarifyChattersByXVertex(String[] allChatsSeparatedByLines, List<RawChatWithVertexDto> rawChatsWithVertices, Integer centerXVertex)  {
        ArrayList<RawChatWithVertexDto> rawChatWithVertexDtos = removeFullChatStringFromChatsArr(rawChatsWithVertices);

//        Arrays.stream(allChatsSeparatedByLines).map((chat) -> {
//            // 문장별로 발화자를 분류
//        });
        // 이름 부분 삭제
        // 전송 시각을 골라서

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
        return new ArrayList<>();
//        return chatDtos;

    }

    private ArrayList<RawChatWithVertexDto> removeFullChatStringFromChatsArr(List<RawChatWithVertexDto> rawChatsWithVertices) {
        ArrayList<RawChatWithVertexDto> rawChatsWithVerticesClone = new ArrayList<>(rawChatsWithVertices);
        rawChatsWithVerticesClone.remove(0);

        return rawChatsWithVerticesClone;
    }
}
