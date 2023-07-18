package paibridge.apiheartee.common;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import paibridge.apiheartee.common.dto.DataResponse;

@RestController
public class TestController {
    
    @RequestMapping("/")
    public DataResponse<String> Test() {
        return new DataResponse("Heartee Server Listening");
    }
}
