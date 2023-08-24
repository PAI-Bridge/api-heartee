package paibridge.apiheartee.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {


    @Value("${jwt.secretKey}")
    private String SECRET_KEY;

    @Value("${jwt.access.expiration}")
    private Long ACCESS_TOKEN_EXPIRATION_TIME;

    @Value("${jwt.refresh.expiration}")
    private Long REFRESH_TOKEN_EXPIRATION_TIME;

    @Value("${jwt.group.name}")
    private String ISSUER;

    private String getOauthId(DecodedJWT token) {
        return token.getClaim("id").asString();
    }

    private String getRole(DecodedJWT token) {
        return token.getClaim("role").asString();
    }

    public String generateAccessToken(Map<String, String> payload) {

        return JWT.create()
            .withIssuedAt(new Date(System.currentTimeMillis()))
            .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
            .withPayload(payload)
            .withIssuer(ISSUER)
            .sign(getSigningKey(SECRET_KEY));
    }

    public String generateRefreshToken(Map<String, String> payload) {

        return JWT.create()
            .withIssuedAt(new Date(System.currentTimeMillis()))
            .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
            .withPayload(payload)
            .withIssuer(ISSUER)
            .sign(getSigningKey(SECRET_KEY));
    }


    public DecodedJWT validateToken(String token) throws JWTVerificationException {
        JWTVerifier verifier = getJWTVerifier();

        return verifier.verify(token);
    }

    private Algorithm getSigningKey(String secretKey) {
        return Algorithm.HMAC256(secretKey);
    }

    private JWTVerifier getJWTVerifier() {
        return JWT.require(getSigningKey(SECRET_KEY))
            .withIssuer(ISSUER)
            .build();
    }

    public Authentication getAuthenticate(DecodedJWT token) throws AuthenticationException {
        String principle = token.getPayload();
        String password = "";
        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(
            getRole(token));

        return new UsernamePasswordAuthenticationToken(principle, password, authorityList);
    }
}
