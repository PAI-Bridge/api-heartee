package paibridge.apiheartee.common.dto;

import lombok.Data;

@Data
public class BodyDto<T> {
    private T data;

    public BodyDto(T data) {
        this.data = data;
    }
}
