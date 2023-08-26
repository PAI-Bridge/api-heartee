package paibridge.apiheartee.auth.oauth.handler;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import paibridge.apiheartee.auth.jwt.JwtProvider;

@Component
@Slf4j
@RequiredArgsConstructor
public class OauthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Value("${url.client.root}")
    private String CLIENT_ROOT_URL;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

        String userData = authentication.getName();
        String oauthId = Arrays.stream(userData.split(",")).findFirst().get().substring(4);

        Map<String, String> accessPayload = new HashMap<>();
        accessPayload.put("id", oauthId);
        accessPayload.put("role", "user");

        String accessToken = jwtProvider.generateAccessToken(accessPayload);

        HashMap<String, String> refreshPayload = new HashMap<>();
        refreshPayload.put("id", oauthId);

        String refreshToken = jwtProvider.generateRefreshToken(refreshPayload);

        Cookie accessCookie = new Cookie("accessToken", accessToken);
        accessCookie.setPath("/");

        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setPath("/");

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
        response.sendRedirect(CLIENT_ROOT_URL);
    }
}
