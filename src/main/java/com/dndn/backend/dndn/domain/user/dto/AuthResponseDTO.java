package com.dndn.backend.dndn.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

public class AuthResponseDTO {

    // 카카오 Access/Refresh Token 응답 DTO
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KakaoToken {
        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("refresh_token")
        private String refreshToken;

        @JsonProperty("token_type")
        private String tokenType;

        @JsonProperty("expires_in")
        private Long expiresIn;

        @JsonProperty("refresh_token_expires_in")
        private Long refreshTokenExpiresIn;
    }

    // 카카오 유저 정보 응답 DTO
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KakaoUserInfo {

        private Long id;

        @JsonProperty("kakao_account")
        private KakaoAccount kakaoAccount;

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class KakaoAccount {

            private String email;

            private Profile profile;

            @Getter
            @Setter
            @NoArgsConstructor
            @AllArgsConstructor
            public static class Profile {
                private String nickname;

                @JsonProperty("profile_image_url")
                private String profileImageUrl;
            }
        }
    }

    // ✅ 네이버 Access/Refresh Token 응답 DTO
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NaverToken {
        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("refresh_token")
        private String refreshToken;

        @JsonProperty("token_type")
        private String tokenType;

        @JsonProperty("expires_in")
        private Long expiresIn;

        @JsonProperty("refresh_token_expires_in")
        private Long refreshTokenExpiresIn;
    }

    // ✅ 네이버 유저 정보 응답 DTO (카카오 스타일)
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NaverUserInfo {

        private Long id;

        @JsonProperty("naver-account")
        private NaverAccount naverAccount;

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class NaverAccount {
            private String email;

            private Profile profile;

            @Getter
            @Setter
            @NoArgsConstructor
            @AllArgsConstructor
            public static class Profile {
                private String nickname;

                @JsonProperty("profile_image_url")
                private String profileImageUrl;
            }
        }
    }

    @Data
    public static class GoogleToken {
        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("expires_in")
        private int expiresIn;

        @JsonProperty("refresh_token")
        private String refreshToken;

        @JsonProperty("scope")
        private String scope;

        @JsonProperty("token_type")
        private String tokenType;

        @JsonProperty("id_token")
        private String idToken;
    }


    // ✅ 구글 유저 정보 응답 DTO (네이버 스타일)
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GoogleUserInfo {

        private String id;

        @JsonProperty("google-account")
        private GoogleAccount googleAccount;

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class GoogleAccount {

            private String email;

            private Profile profile;

            @Getter
            @Setter
            @NoArgsConstructor
            @AllArgsConstructor
            public static class Profile {

                private String name;

                @JsonProperty("picture")
                private String profileImageUrl;
            }
        }
    }



    // 최종 로그인 응답 DTO
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResult {
        private String accessToken;
        private String refreshToken;
        private boolean isNewUser;
        private String sosialAccessToken;
        private String sosialRefreshToken;
    }
}
