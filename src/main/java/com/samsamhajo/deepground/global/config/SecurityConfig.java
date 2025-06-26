package com.samsamhajo.deepground.global.config;

import com.samsamhajo.deepground.auth.jwt.JwtAuthenticationFilter;
import com.samsamhajo.deepground.auth.jwt.JwtProvider;
import com.samsamhajo.deepground.auth.oauth.CustomOAuth2UserService;
import com.samsamhajo.deepground.auth.oauth.OAuth2AuthenticationSuccessHandler;
import com.samsamhajo.deepground.auth.security.CustomUserDetailsService;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    private static final String[] PERMIT_URL_ARRAY = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**",
            "/v3/api-docs.yaml",
            "/auth/**",
            "/ws/**",
            "/auth/**",
//            "/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            JwtProvider jwtProvider,
            CustomUserDetailsService userDetailsService,
            MemberRepository memberRepository) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(PERMIT_URL_ARRAY).permitAll()
                        .anyRequest().authenticated())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                    .authenticationEntryPoint((request, response, authException) -> {
                        Logger logger = LoggerFactory.getLogger("SecurityExceptionLogger");

                        // 로그 출력
                        logger.warn("인증 실패: {} - {}",
                            request.getRequestURI(),
                            authException.getMessage());
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType("application/json");
                        response.getWriter().write("{\"message\": \"Unauthorized\"}");
                    })
                )
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtProvider, userDetailsService, memberRepository),
                        UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService))
                        .redirectionEndpoint(redirection -> redirection
                                .baseUri("/auth/oauth/*/callback"))
                        .successHandler(oAuth2AuthenticationSuccessHandler));

        return http.build();
    }
}
