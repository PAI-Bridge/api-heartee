package paibridge.apiheartee.common.dto;

import java.util.List;
import lombok.Data;

// BodyDto를 상속해서 만들고 싶은데
@Data
public class BodyArrayDto<T> {
    private Integer count;
    private List<T> data;

    public BodyArrayDto(List<T> data) {
        this.count = data.size();
        this.data = data;
    }
}
