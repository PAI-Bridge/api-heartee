package paibridge.apiheartee.auth.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        try {
            String token = CookieUtil.getCookie(httpRequest, "accessToken").getValue();

            //토큰 유효성 검증
            DecodedJWT decodedJWT = jwtProvider.validateToken(token);

            request.setAttribute("user", decodedJWT.getPayload());
            Authentication authenticate = jwtProvider.getAuthenticate(decodedJWT);
            SecurityContextHolder.getContext().setAuthentication(authenticate);

        } catch (Exception e) {
            log.info("accessToken이 없거나 유효하지 않음");
            // refreshToken이 유효하면 accessToken을 재발급하여 response하고 authentication도 정상 처리
            try {
                String refreshToken = CookieUtil.getCookie(httpRequest, "refreshToken").getValue();

                DecodedJWT decodedRefreshJWT = jwtProvider.validateToken(refreshToken);

                // accessToken 재발급
                Map<String, String> newAccessPayload = new HashMap<>();
                newAccessPayload.put("id", decodedRefreshJWT.getClaim("id").asString());
                newAccessPayload.put("role", "user");
                String newAccessToken = jwtProvider.generateAccessToken(newAccessPayload);
                //

                Cookie newAccessCookie = new Cookie("accessToken", newAccessToken);
                newAccessCookie.setPath("/");
                response.addCookie(newAccessCookie);

                request.setAttribute("user", newAccessPayload);
                Authentication authenticate = jwtProvider.getAuthenticate(
                    jwtProvider.validateToken(newAccessToken));
                SecurityContextHolder.getContext().setAuthentication(authenticate);


            } catch (Exception re) {
                log.info("refreshToken이 없거나 유효하지 않음");
            }
        }
        filterChain.doFilter(request, response);
    }
}
