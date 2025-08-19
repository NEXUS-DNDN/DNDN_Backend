package com.dndn.backend.dndn.global.config;

import com.dndn.backend.dndn.global.config.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CORS 설정
                .cors(c -> c.configurationSource(corsConfigurationSource()))
                // CSRF 보호 비활성화 (JWT 방식에서는 불필요)
                .csrf(csrf -> csrf.disable())
                // 기본 로그인 폼 비활성화
                .formLogin(form -> form.disable())
                // HTTP Basic 인증 비활성화
                .httpBasic(httpBasic -> httpBasic.disable())
                //️ 세션 사용 안 함 (JWT 기반 stateless 인증)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 인가 규칙 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/dndn").permitAll()
                        .requestMatchers("/profile").permitAll()
                        .requestMatchers("/test/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/login/**").permitAll()
                        .requestMatchers("/welfare/**").permitAll()
                        .requestMatchers(
                                "/user/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                // JWT 인증 필터 등록 (기본 인증 필터 앞에 추가)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ✅ CORS 설정: 배포/로컬 오리진 허용 + JWT 헤더/쿠키 허용
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // 꼭 필요한 오리진만 명시하세요 (credentials=true이면 * 불가)
        config.setAllowedOrigins(List.of(
                "",                               // 배포 프론트
                "http://localhost:3000",          // 로컬 프론트
                "http://127.0.0.1:3000"
        ));

        // 사용 메서드
        config.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));

        // 프런트에서 보낼 수 있는 헤더 (JWT는 Authorization 필요)
        config.setAllowedHeaders(List.of("*"));

        // 브라우저가 접근 가능한 응답 헤더 (필요 시)
        config.setExposedHeaders(List.of("*"));

        // 쿠키/인증정보 전송 허용 (JWT를 헤더로 쓰더라도 SPA에서 쿠키 쓰면 필요)
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
