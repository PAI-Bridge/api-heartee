package paibridge.apiheartee.conversation.service.image;

import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BoundingPoly;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Vertex;
import org.springframework.stereotype.Service;
import paibridge.apiheartee.conversation.service.image.dto.*;

import java.io.IOException;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        Collections.sort(rawChatWithVertexDtos, new YVertexComperator());

        // 4. 추출한 텍스트 중, 전송 시각을 표시하는 텍스트는 채팅을 전송한 시점으로 포함.
        ArrayList<ConcattedChatWithVertexAndTime> concattedChatsWithVertexAndTime = setChatSentTime(rawChatWithVertexDtos);
//        concattedChatsWithVertexAndTime.forEach(concattedChatWithVertexAndTime -> {
//            System.out.println("Chat= " + concattedChatWithVertexAndTime.getChat() + " SentTime= " + concattedChatWithVertexAndTime.getSentTime());
//        });

        // 5. 중앙 X 좌표값으로 발화자를 분류
        List<ChatDto> chatDtos = clarifyAuthorsByXVertex(concattedChatsWithVertexAndTime, imageCenterXVertex);
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

    private ArrayList<ConcattedChatWithVertexAndTime> setChatSentTime(ArrayList<RawChatWithVertexDto> rawChatWithVertexDtos) {
        HashMap<Integer, String> idxSentTimeHashMap = new HashMap<>();

        Integer lastTimeTextIdx = 0;

        rawChatWithVertexDtos.forEach(rawChatWithVertexDto -> {
            String chat = rawChatWithVertexDto.getChat();
            int idx = indexOf(rawChatWithVertexDtos, rawChatWithVertexDto);

            if (isTimeText(chat)) {
                for (int i = lastTimeTextIdx; i < idx; i++) {
                    // 오후, 또는 오전이라는 문자열은 제외함
                    idxSentTimeHashMap.put(i, chat.replaceAll("오후|오전", "").replaceAll("\\s", ""));
                }

            }
        });

        List<ConcattedChatWithVertexAndTime> concattedChatsWithVertexAndTime = rawChatWithVertexDtos.stream().map(rawChatWithVertexDto -> {
            Integer idx = indexOf(rawChatWithVertexDtos, rawChatWithVertexDto);

            // 해당 인덱스의 문자열이 시간이면, 제외함.
            if (isTimeText(rawChatWithVertexDto.getChat())) return null;

            String sentTime = idxSentTimeHashMap.get(idx);

            return ConcattedChatWithVertexAndTime.concattedChatWithVertexAndTimeBuilder()
                    .xVertexStart(rawChatWithVertexDto.getXVertexStart())
                    .xVertexEnd(rawChatWithVertexDto.getXVertexEnd())
                    .yVertexStart(rawChatWithVertexDto.getYVertexStart())
                    .yVertexEnd(rawChatWithVertexDto.getYVertexEnd())
                    .chat(rawChatWithVertexDto.getChat())
                    .sentTime(sentTime)
                    .build();
        }).filter(dto -> {
            return dto != null;
        }).collect(Collectors.toList());

        return (ArrayList<ConcattedChatWithVertexAndTime>) concattedChatsWithVertexAndTime;
    }

    private Integer indexOf(ArrayList<RawChatWithVertexDto> rawChatWithVertexDtos, RawChatWithVertexDto rawChatWithVertexDto) {
        for (int i = 0; i < rawChatWithVertexDtos.size(); i++) {
            RawChatWithVertexDto rawChatWithVertexDtoInArr = rawChatWithVertexDtos.get(i);
            if (rawChatWithVertexDtoInArr.equals(rawChatWithVertexDto)) return i;
        }
        return -1;
    }

    private Boolean isTimeText(String text) {
        String timeRegex = "^(오전|오후)?(\\s?\\d+[:]?\\d*)$";

        Pattern pattern = Pattern.compile(timeRegex);

        return pattern.matcher(text).matches();
    }

    private List<ChatDto> clarifyAuthorsByXVertex(ArrayList<ConcattedChatWithVertexAndTime> concattedChatsWithVertexAndTime, Integer centerXVertex)  {
        ArrayList<ConcattedChatWithVertexAndTime> rawChatsWithoutFullChat = removeFullChatStringFromChatsArr(concattedChatsWithVertexAndTime);

         List<ChatDto> chatDtos = rawChatsWithoutFullChat.stream().map((chat) -> {
            Integer xVertexStart = chat.getXVertexStart();
            Integer xVertexEnd = chat.getXVertexEnd();
             System.out.println("Clarifying Each Chat= " + chat.getChat());

            Integer centerXVertexOfChat = (xVertexStart + xVertexEnd) / 2;

            if (centerXVertexOfChat < centerXVertex) return ChatDto.builder()
                    .content(chat.getChat())
                    .author(Author.partner)
                    .time(chat.getSentTime())
                    .build();

            return ChatDto.builder()
                    .content(chat.getChat())
                    .author(Author.user)
                    .time(chat.getSentTime())
                    .build();

        }).collect(Collectors.toList());

        return chatDtos;
    }

    private ArrayList<RawChatWithVertexDto> concatWordsOfSameLine(List<RawChatWithVertexDto> rawChatsWithVertices) {
        HashMap<String, RawChatWithVertexDto> wordsInSameLineConcat = new HashMap<String, RawChatWithVertexDto>();

        rawChatsWithVertices.stream().forEach(rawChatWithVertex -> {
            String chat = rawChatWithVertex.getChat();
            Integer yVertexStart = rawChatWithVertex.getYVertexStart();
            Integer yVertexEnd = rawChatWithVertex.getYVertexEnd();

            String yVertexSeparator = "-";
            String key = yVertexStart.toString() + yVertexSeparator + yVertexEnd.toString();

            RawChatWithVertexDto concattedWordsOfSameLine = getConcattedWordsOfSameLine(wordsInSameLineConcat, rawChatWithVertex, yVertexSeparator);

            if (concattedWordsOfSameLine != null) {
                RawChatWithVertexDto rawChatWithVertexDtoConcatted = concattedWordsOfSameLine;
                String concattedChatString = rawChatWithVertexDtoConcatted.getChat();

                String concatChatUpdated = spacingRequired(rawChatWithVertexDtoConcatted, rawChatWithVertex)
                        ? concattedChatString + " " + chat
                        : concattedChatString + chat;

                rawChatWithVertexDtoConcatted.setChat(concatChatUpdated);
                rawChatWithVertexDtoConcatted.setXVertexEnd(rawChatWithVertex.getXVertexEnd());
                
            } else {
                wordsInSameLineConcat.put(key, rawChatWithVertex);
            }
        });

        return new ArrayList<>(wordsInSameLineConcat.values());
    }

    private RawChatWithVertexDto getConcattedWordsOfSameLine(HashMap<String, RawChatWithVertexDto> wordsHashMap, RawChatWithVertexDto currentRawChatWithVertexDto, String yVertexSeparator) {
        // 같은 줄인 것으로 간주할 Y 좌표의 버퍼
        Integer yVertexSameLineBuffer = 10;

        for (Map.Entry<String, RawChatWithVertexDto> entry:wordsHashMap.entrySet()) {
            String key = entry.getKey();
            String[] yVertexes = key.split(yVertexSeparator);
            Integer concattedyVertexStart = Integer.parseInt(yVertexes[0]);
            Integer concattedyVertexEnd = Integer.parseInt(yVertexes[1]);

            if (Math.abs(concattedyVertexStart - currentRawChatWithVertexDto.getYVertexStart()) < yVertexSameLineBuffer
                    && Math.abs(concattedyVertexEnd - currentRawChatWithVertexDto.getYVertexEnd()) < yVertexSameLineBuffer) return entry.getValue();
        }
        return null;


    }
    private Boolean spacingRequired(RawChatWithVertexDto rawChatWithVertexConcatted, RawChatWithVertexDto currentRawChatWithVertex) {
        Integer spacingCriteriaVertex = 8;

        Integer xVertexEndOfConcatted = rawChatWithVertexConcatted.getXVertexEnd();

        Integer xVertexStartOfNext = currentRawChatWithVertex.getXVertexStart();

        // 기존 문자열 x좌표에서 다음 문자열의 X좌표까지의 거리가 8을 초과하면, 띄어쓰기가 되어 있던 것으로 간주함.
        return Math.abs(xVertexEndOfConcatted - xVertexStartOfNext) > spacingCriteriaVertex;
    }
    private ArrayList<ConcattedChatWithVertexAndTime> removeFullChatStringFromChatsArr(List<ConcattedChatWithVertexAndTime> concattedChatsWithVertexAndTime) {
        ArrayList<ConcattedChatWithVertexAndTime> rawChatsWithVerticesClone = new ArrayList<>(concattedChatsWithVertexAndTime);
        rawChatsWithVerticesClone.remove(0);

        return rawChatsWithVerticesClone;
    }
}

// YVertex 기준 오름차순 정렬
class YVertexComperator implements Comparator<RawChatWithVertexDto> {
    @Override
    public int compare(RawChatWithVertexDto o1, RawChatWithVertexDto o2) {
        if (o1.getYVertexStart() > o2.getYVertexStart()) return 1;
        else if (o1.getYVertexStart() < o2.getYVertexStart()) return -1;
        else return 0;
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