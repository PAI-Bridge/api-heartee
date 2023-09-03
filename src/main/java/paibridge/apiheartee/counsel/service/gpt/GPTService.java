package paibridge.apiheartee.counsel.service.gpt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import paibridge.apiheartee.counsel.entity.CounselReport;
import paibridge.apiheartee.counsel.entity.CounselReportBU;
import paibridge.apiheartee.counsel.entity.CounselReportDT;
import paibridge.apiheartee.counsel.entity.CounselReportGL;
import paibridge.apiheartee.counsel.repository.CounselReportRepository;
import paibridge.apiheartee.counsel.service.gpt.dto.GPTCounselJsonResDto;
import paibridge.apiheartee.counsel.service.gpt.dto.GPTCounselReportSaveOptionsDto;
import paibridge.apiheartee.counsel.service.gpt.dto.GPTCounselRequestDto;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class GPTService {
    private final CounselReportRepository counselReportRepository;

    @Value("${gpt.baseurl}")
    private String gptServerBaseURl;

    @Async
    public void fetchCounsel(GPTCounselRequestDto request, GPTCounselReportSaveOptionsDto saveOptions) throws IOException {
        // FIXME : 로직 분리
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String jsonPayload = buildCounselRequestPayload(request);
        try {
            HttpUriRequest postRequest = RequestBuilder.post()
                    .setUri(gptServerBaseURl + "/gpt/heartee")
                    .setEntity(new StringEntity(jsonPayload, "application/json", "UTF-8"))
                    .build();

            System.out.println("SENDING REQUEST TO GPT...");

            CloseableHttpResponse res = httpClient.execute(postRequest);

            try {
                String jsonString = EntityUtils.toString(res.getEntity());

                String summary = extractSummaryFromRes(jsonString);
                GPTCounselJsonResDto json = extractJsonFromRes(jsonString);
//                GPTCounselResponseDto gptCounselResponseDto = new Gson().fromJson(jsonString, GPTCounselResponseDto.class);
//                System.out.println("gptCounselResponseDto = " + gptCounselResponseDto.toString());
//                CounselReport counselReport = buildEntityToSave(gptCounselResponseDto, saveOptions);
//                this.counselReportRepository.save(counselReport);

            } finally {
                res.close();
            }
        }
        finally {
            httpClient.close();
        }
    }

    private String buildCounselRequestPayload(GPTCounselRequestDto request) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPayload = objectMapper.writeValueAsString(request);

        return jsonPayload;
    }

    private String extractSummaryFromRes(String responseString) throws IllegalArgumentException {
        String summaryPattern = "<(.*?)>";

        Pattern pattern = Pattern.compile(summaryPattern);
        Matcher matcher = pattern.matcher(responseString);

        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new IllegalArgumentException("Invalid response string");
    }

    private GPTCounselJsonResDto extractJsonFromRes(String responseString) throws IllegalArgumentException {
        String summaryPattern = "\\{(.*?)\\}";

        Pattern pattern = Pattern.compile(summaryPattern);
        Matcher matcher = pattern.matcher(responseString);

        if (matcher.find()) {
            String jsonString = "{" + matcher.group(1) + "}";
            GPTCounselJsonResDto gptCounselJsonResDto = new Gson().fromJson(jsonString, GPTCounselJsonResDto.class);

            return gptCounselJsonResDto;

        }
        throw new IllegalArgumentException("Invalid response string");
    }

    private CounselReport buildEntityToSave(String summary, GPTCounselJsonResDto res, GPTCounselReportSaveOptionsDto saveOptions) {
        System.out.println("res = " + res.toString());
        String dType = saveOptions.getDType();

        if (dType.equals("DT")) {
            return CounselReportDT.builder()
                    .counselRequest(saveOptions.getCounselRequest())
                    .summary(summary)
                    .solution(res.getSolution())
                    .willingness(Integer.parseInt(res.getWillingness()))
                    .selfOpenness(Integer.parseInt(res.getSelfOpenness()))
                    .voiceOver(Integer.parseInt(res.getVoiceover()))
                    .positiveLanguage(Integer.parseInt(res.getPositiveLanguage()))
                    .frequency(Integer.parseInt(res.getFrequency()))
                    .explanation(res.getExplanation())
                    .build();
        }
        if (dType.equals("BU")) {
                return CounselReportBU.builder()
                        .counselRequest(saveOptions.getCounselRequest())
                        .summary(summary)
                        .solution(res.getSolution())
                        .willingness(Integer.parseInt(res.getWillingness()))
                        .selfOpenness(Integer.parseInt(res.getSelfOpenness()))
                        .voiceOver(Integer.parseInt(res.getVoiceover()))
                        .positiveLanguage(Integer.parseInt(res.getPositiveLanguage()))
                        .frequency(Integer.parseInt(res.getFrequency()))
                        .explanation(res.getExplanation())
                        .build();
            }
        if (dType.equals("GL")) {
                return CounselReportGL.builder()
                        .counselRequest(saveOptions.getCounselRequest())
                        .summary(summary)
                        .solution(res.getSolution())
                        .willingness(Integer.parseInt(res.getWillingness()))
                        .selfOpenness(Integer.parseInt(res.getSelfOpenness()))
                        .voiceOver(Integer.parseInt(res.getVoiceover()))
                        .positiveLanguage(Integer.parseInt(res.getPositiveLanguage()))
                        .frequency(Integer.parseInt(res.getFrequency()))
                        .explanation(res.getExplanation())
                        .build();
            }

        throw new RuntimeException("Invalid dType");
    }
}

