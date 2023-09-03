package paibridge.apiheartee.counsel.service.gpt.dto;


import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
public class GPTCounselJsonResDto {
    @SerializedName("willingness")
    private String willingness;

    @SerializedName("self_openness")
    private String selfOpenness;

    @SerializedName("voiceover")
    private String voiceover;

    @SerializedName("positive_language")
    private String positiveLanguage;

    @SerializedName("frequency")
    private String frequency;

    @SerializedName("explanation")
    private String explanation;

    @SerializedName("solution")
    private String solution;

    @Builder
    public GPTCounselJsonResDto(String willingness, String selfOpenness, String voiceover, String positiveLanguage, String frequency, String explanation, String solution) {
        this.willingness = willingness;
        this.selfOpenness = selfOpenness;
        this.voiceover = voiceover;
        this.positiveLanguage = positiveLanguage;
        this.frequency = frequency;
        this.explanation = explanation;
        this.solution = solution;
    }



    public static GPTCounselJsonResDto create(String willingness, String selfOpenness, String voiceover, String positiveLanguage, String frequency, String explanation, String solution) {
        return GPTCounselJsonResDto.builder()
                    .willingness(willingness)
                    .selfOpenness(selfOpenness)
                    .voiceover(voiceover)
                    .positiveLanguage(positiveLanguage)
                    .frequency(frequency)
                    .explanation(explanation)
                    .solution(solution)
                .   build();
    }
}
