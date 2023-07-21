package paibridge.apiheartee.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DataResponse<T> {
    private T data;

    public DataResponse(T data) {
        this.data = data;
    }
}
