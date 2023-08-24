package paibridge.apiheartee.auth.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
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

        } catch (NullPointerException e) {
            log.info("accessToken 없음");
        } catch (JWTVerificationException e) {
            log.info("유효하지 않은 토큰");
        }
        filterChain.doFilter(request, response);
    }
}
