package paibridge.apiheartee.counsel.service.gpt;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import paibridge.apiheartee.conversation.repository.TempConversationRepository;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class GPTService {
    private final TempConversationRepository tempConversationRepository;

    @Value("${gpt.baseurl}")
    private String gptServerBaseURl;

    @Async
    public GPTCounselResponseDto callCounselGPT(GPTCounselRequestDto request) throws IOException {
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
                return new Gson().fromJson(jsonString, GPTCounselResponseDto.class);
            } finally {
                res.close();
            }
        }
        finally {
            httpClient.close();
        }
    }
}
