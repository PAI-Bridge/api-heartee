package paibridge.apiheartee.counsel.service.gpt;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
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
import paibridge.apiheartee.counsel.service.gpt.dto.GPTCounselReportSaveOptionsDto;
import paibridge.apiheartee.counsel.service.gpt.dto.GPTCounselRequestDto;
import paibridge.apiheartee.counsel.service.gpt.dto.GPTCounselResponseDto;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class GPTService {
    private final CounselReportRepository counselReportRepository;

    @Value("${gpt.baseurl}")
    private String gptServerBaseURl;

    @Async
    public void fetchCounselFromGPT(GPTCounselRequestDto request, GPTCounselReportSaveOptionsDto saveOptions) throws IOException {
        // FIXME : 로직 분리
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            HttpUriRequest postRequest = RequestBuilder.post()
                    .setUri(gptServerBaseURl + "/gpt/heartee")
                    .addParameter("language", request.getLanguage())
                    .addParameter("gender_user", request.getUserGender())
                    .addParameter("gender_counterpart", request.getCounterpartGender())
                    .addParameter("conversation_history", request.getConversationHistory())
                    .build();

            CloseableHttpResponse res = httpClient.execute(postRequest);

            try {
                String jsonString = EntityUtils.toString(res.getEntity());
                GPTCounselResponseDto gptCounselResponseDto = new Gson().fromJson(jsonString, GPTCounselResponseDto.class);

                CounselReport counselReport = buildEntityToSave(gptCounselResponseDto, saveOptions);
                this.counselReportRepository.save(counselReport);

            } finally {
                res.close();
            }
        }
        finally {
            httpClient.close();
        }
    }

    private CounselReport buildEntityToSave(GPTCounselResponseDto res, GPTCounselReportSaveOptionsDto saveOptions) {
        String dType = saveOptions.getDType();
        // FIXME : Hoxfix : GPT 서버에서 정상 응답값을 확인하지 못해서 현재 빈 상태. 수정 필요
        if (dType.equals("DT")) {
            return CounselReportDT.builder()
                    .build();
        }
        if (dType.equals("BU")) {
                return CounselReportBU.builder().build();
            }
        if (dType.equals("GL")) {
                return CounselReportGL.builder().build();
            }

        throw new RuntimeException("Invalid dType");
    }
}
