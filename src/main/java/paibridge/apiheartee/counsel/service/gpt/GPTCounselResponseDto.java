package paibridge.apiheartee.counsel.service.gpt;

public class GPTCounselResponseDto {

    private GPTCounselResponseDetail[] detail;
}

class GPTCounselResponseDetail {
    private String[] loc;
    private String msg;
    private String type;

}