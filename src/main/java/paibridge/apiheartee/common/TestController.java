package paibridge.apiheartee.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import paibridge.apiheartee.common.dto.BodyDto;

@RestController
public class TestController {

    @RequestMapping("/")
    public BodyDto<String> Test() {
        return new BodyDto("Heartee Server Listening");
    }
}
