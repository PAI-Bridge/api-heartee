package paibridge.apiheartee.auth.oauth;

import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import paibridge.apiheartee.auth.jwt.JwtProvider;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final JwtProvider jwtProvider;

    @GetMapping("/refresh")
    public ResponseEntity<String> refreshAccessToken(
        @CookieValue(name = "refreshToken") String refreshToken,
        HttpServletResponse response
    ) {
        try {
            DecodedJWT decodedJWT = jwtProvider.validateToken(refreshToken);
            String oauthId = decodedJWT.getClaim("id").asString();

            Map<String, String> accessPayload = new HashMap<>();
            accessPayload.put("id", oauthId);
            accessPayload.put("role", "USER");
            String accessToken = jwtProvider.generateAccessToken(accessPayload);

            Cookie accessCookie = new Cookie("accessToken", accessToken);
            accessCookie.setPath("/");

            response.addCookie(accessCookie);

        } catch (Exception e) {
            log.info("refreshToken이 유효하지 않음");
            return new ResponseEntity<>("invalid refreshToken", HttpStatus.UNAUTHORIZED);
        }

        log.info("accessToken 재발급 완료");
        return new ResponseEntity<>("accessToken reissued", HttpStatus.OK);
    }
}
