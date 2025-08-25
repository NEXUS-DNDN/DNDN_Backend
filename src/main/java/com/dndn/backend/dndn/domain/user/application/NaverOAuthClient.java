package com.dndn.backend.dndn.domain.user.application;

import com.dndn.backend.dndn.domain.user.dto.AuthResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public AuthResponseDTO.NaverToken requestToken(String code, String state) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", clientId);
            params.add("client_secret", clientSecret);
            params.add("redirect_uri", redirectUri);
            params.add("code", code);
            params.add("state", state);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

            // ✅ JSON 문자열로 먼저 받기
            ResponseEntity<String> rawResponse = restTemplate.postForEntity(tokenUri, request, String.class);
            log.info("네이버 토큰 응답 JSON 원본: {}", rawResponse.getBody());

            // ✅ ObjectMapper로 수동 매핑
            ObjectMapper mapper = new ObjectMapper();
            AuthResponseDTO.NaverToken token = mapper.readValue(rawResponse.getBody(), AuthResponseDTO.NaverToken.class);
            log.info("매핑된 accessToken={}", token.getAccessToken());

            return token;

        } catch (Exception e) {
            log.error("❌ 네이버 토큰 요청 실패: {}", e.getMessage(), e);
            throw new RuntimeException("네이버 토큰 요청 실패", e);
        }
    }

    public AuthResponseDTO.NaverUserInfo requestUserInfo(String accessToken) {
        try {
            log.info("accessToken: {}", accessToken);
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
