package paibridge.apiheartee.common.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

// BodyDto를 상속해서 만들고 싶은데
@Data
@NoArgsConstructor
public class DataArrayResponse<T> {
    private Integer count;
    private List<T> data;

    public DataArrayResponse(List<T> data) {
        this.count = data.size();
        this.data = data;
    }
}
