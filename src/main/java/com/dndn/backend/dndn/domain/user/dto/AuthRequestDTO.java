package com.dndn.backend.dndn.domain.user.dto;

import com.dndn.backend.dndn.domain.model.enums.LoginType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthRequestDTO {

    // 카카오 로그인을 통해 발급받은 accessToken 을 서버에 전달
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SocialLoginRequest {
        private LoginType loginType;

        // 카카오 / 구글 → accessToken 사용
        private String accessToken;

        // 네이버 → code + state 사용
        private String code;
        private String state;
    }


    // 클라이언트가 토큰 재발급 요청을 보낼 때 사용하는 요청 데이터 객체
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefreshToken {
        private Long userId;
        private String refreshToken;
    }
}
