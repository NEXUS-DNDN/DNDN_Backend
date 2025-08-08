package com.dndn.backend.dndn.domain.user.application;

import com.dndn.backend.dndn.domain.user.dto.AuthResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverOAuthClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${spring.oauth.naver.client-id}")
    private String clientId;

    @Value("${spring.oauth.naver.client-secret}")
    private String clientSecret;

    @Value("${spring.oauth.naver.redirect-uri}")
    private String redirectUri;

    @Value("${spring.oauth.naver.token-uri}")
    private String tokenUri;

    @Value("${spring.oauth.naver.user-info-uri}")
    private String userInfoUri;

    public AuthResponseDTO.NaverToken requestToken(String code) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", clientId);
            params.add("client_secret", clientSecret);
            params.add("redirect_uri", redirectUri);
            params.add("code", code);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

            ResponseEntity<AuthResponseDTO.NaverToken> response =
                    restTemplate.postForEntity(tokenUri, request, AuthResponseDTO.NaverToken.class);

            return Optional.ofNullable(response.getBody())
                    .orElseThrow(() -> new RuntimeException("네이버 토큰 응답이 null입니다."));
        } catch (Exception e) {
            log.error("❌ 네이버 토큰 요청 실패: {}", e.getMessage(), e);
            throw new RuntimeException("네이버 토큰 요청 실패", e);
        }
    }

    public AuthResponseDTO.NaverUserInfo requestUserInfo(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.set("Authorization", "Bearer " + accessToken);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<AuthResponseDTO.NaverUserInfo> response =
                    restTemplate.exchange(userInfoUri, HttpMethod.GET, entity, AuthResponseDTO.NaverUserInfo.class);

            return Optional.ofNullable(response.getBody())
                    .orElseThrow(() -> new RuntimeException("네이버 사용자 정보 응답이 null입니다."));
        } catch (Exception e) {
            log.error("❌ 네이버 사용자 정보 요청 실패: {}", e.getMessage(), e);
            throw new RuntimeException("네이버 사용자 정보 요청 실패", e);
        }
    }
}
