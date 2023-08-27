package paibridge.apiheartee.common.config;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import paibridge.apiheartee.auth.jwt.JwtAuthenticationFilter;
import paibridge.apiheartee.auth.oauth.OauthService;
import paibridge.apiheartee.auth.oauth.handler.OauthAuthenticationFailureHandler;
import paibridge.apiheartee.auth.oauth.handler.OauthAuthenticationSuccessHandler;

@RequiredArgsConstructor
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig {

    private final OauthService oauthService;
    private final OauthAuthenticationSuccessHandler oauthAuthenticationSuccessHandler;
    private final OauthAuthenticationFailureHandler oauthAuthenticationFailureHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${url.cors-allow}")
    private List<String> CORS_ALLOW_URLS;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors().configurationSource(corsConfigurationSource())
            .and()
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
            .authorizeRequests()
            .antMatchers("/auth/**", "/login/**", "/oauth2/**", "/").permitAll()
            .anyRequest().authenticated().and()
            .exceptionHandling()
            .authenticationEntryPoint((request, response, authException) -> {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            });

        //oauth2Login
        http.oauth2Login()
            .userInfoEndpoint().userService(oauthService)  // 회원 정보 처리
            .and()
            .successHandler(oauthAuthenticationSuccessHandler)
            .failureHandler(oauthAuthenticationFailureHandler);

        //jwt filter 설정
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        for (String corsAllowUrl : CORS_ALLOW_URLS) {
            configuration.addAllowedOrigin(corsAllowUrl);
        }
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
