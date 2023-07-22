package paibridge.apiheartee.common;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import paibridge.apiheartee.common.dto.DataResponse;

@Slf4j
@RestController
@RequestMapping("/")
public class TestController {

    @RequestMapping
    public DataResponse<String> getTest() {
        return new DataResponse<>("Heartee Server Listening");
    }

    @PostMapping
    public DataResponse<TestDto> postTest(@RequestBody TestDto testDto) {
        return new DataResponse<>(testDto);
    }
}

@Data
class TestDto {

    private String text;
}
