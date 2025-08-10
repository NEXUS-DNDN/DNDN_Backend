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
public class GooGleOAuthClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${spring.oauth.google.client-id}")
    private String clientId;

    @Value("${spring.oauth.google.client-secret}")
    private String clientSecret;

    @Value("${spring.oauth.google.redirect-uri}")
    private String redirectUri;

    @Value("${spring.oauth.google.token-uri}")
    private String tokenUri;

    @Value("${spring.oauth.google.user-info-uri}")
    private String userInfoUri;

    /**
     * 액세스 토큰 요청
     */
    public AuthResponseDTO.GoogleToken requestToken(String code) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("code", code);
            params.add("client_id", clientId);
            params.add("client_secret", clientSecret);
            params.add("redirect_uri", redirectUri);
            params.add("grant_type", "authorization_code");

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

            ResponseEntity<AuthResponseDTO.GoogleToken> response =
                    restTemplate.postForEntity(tokenUri, request, AuthResponseDTO.GoogleToken.class);

            return Optional.ofNullable(response.getBody())
                    .orElseThrow(() -> new RuntimeException("구글 토큰 응답이 null입니다."));
        } catch (Exception e) {
            log.error("❌ 구글 토큰 요청 실패: {}", e.getMessage(), e);
            throw new RuntimeException("구글 토큰 요청 실패", e);
        }
    }

    /**
     * 사용자 정보 요청
     */
    public AuthResponseDTO.GoogleUserInfo requestUserInfo(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken); // Authorization: Bearer {token}

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<AuthResponseDTO.GoogleUserInfo> response =
                    restTemplate.exchange(userInfoUri, HttpMethod.GET, entity, AuthResponseDTO.GoogleUserInfo.class);

            return Optional.ofNullable(response.getBody())
                    .orElseThrow(() -> new RuntimeException("구글 사용자 정보 응답이 null입니다."));
        } catch (Exception e) {
            log.error("❌ 구글 사용자 정보 요청 실패: {}", e.getMessage(), e);
            throw new RuntimeException("구글 사용자 정보 요청 실패", e);
        }
    }
}
