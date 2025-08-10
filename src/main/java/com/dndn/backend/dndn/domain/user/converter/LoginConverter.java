package com.dndn.backend.dndn.domain.user.converter;

import com.dndn.backend.dndn.domain.user.dto.AuthResponseDTO;

public class LoginConverter {

    // 카카오 로그인 앱용
    public static AuthResponseDTO.LoginResult toKAKAOLoginResponse(
            String accessToken,
            String refreshToken,
            String kakaoAccessToken,
            boolean isNewUser
    ) {
        return AuthResponseDTO.LoginResult.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .sosialAccessToken(kakaoAccessToken)
                .isNewUser(isNewUser)
                .build();
    }

    // 카카오 로그인 웹용
    public static AuthResponseDTO.LoginResult toKAKAOWebLoginResponse(
            String jwtAccessToken,
            String jwtRefreshToken,
            boolean isNewUser,
            AuthResponseDTO.KakaoToken token
    ) {
        return AuthResponseDTO.LoginResult.builder()
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
                .isNewUser(isNewUser)
                .sosialAccessToken(token.getAccessToken())
                .sosialRefreshToken(token.getRefreshToken())
                .build();
    }

    // 네이버 앱 로그인
    public static AuthResponseDTO.LoginResult toNAVERLoginResponse(
            String accessToken,
            String refreshToken,
            String naverAccessToken,
            boolean isNewUser
    ) {
        return AuthResponseDTO.LoginResult.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .sosialAccessToken(naverAccessToken)
                .isNewUser(isNewUser)
                .build();
    }

    // 네이버 웹 로그인
    public static AuthResponseDTO.LoginResult toNAVERWebLoginResponse(
            String jwtAccessToken,
            String jwtRefreshToken,
            boolean isNewUser,
            AuthResponseDTO.NaverToken token
    ) {
        return AuthResponseDTO.LoginResult.builder()
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
                .isNewUser(isNewUser)
                .sosialAccessToken(token.getAccessToken())
                .sosialRefreshToken(token.getRefreshToken())
                .build();
    }

    // 구글 웹 로그인
    public static AuthResponseDTO.LoginResult toGOOGLEWebLoginResponse(
            String jwtAccessToken,
            String jwtRefreshToken,
            boolean isNewUser,
            AuthResponseDTO.GoogleToken token
    ) {
        return AuthResponseDTO.LoginResult.builder()
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
                .isNewUser(isNewUser)
                .sosialAccessToken(token.getAccessToken())
                .sosialRefreshToken(token.getRefreshToken())
                .build();
    }

    // 구글 앱 로그인
    public static AuthResponseDTO.LoginResult toGOOGLELoginResponse(
            String accessToken,
            String refreshToken,
            String naverAccessToken,
            boolean isNewUser
    ) {
        return AuthResponseDTO.LoginResult.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .sosialAccessToken(naverAccessToken)
                .isNewUser(isNewUser)
                .build();
    }

    // 토큰 재발급
    public static AuthResponseDTO.LoginResult toRefreshTokenResponse(
            String newAccessToken,
            String newRefreshToken

    ) {
        return AuthResponseDTO.LoginResult.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .sosialAccessToken(null)
                .sosialRefreshToken(null)
                .isNewUser(false)
                .build();
    }

    // 자동 로그인
    public static AuthResponseDTO.AutoLoginResult toAutoLoginResponse(Long userId, boolean valid) {
        return AuthResponseDTO.AutoLoginResult.builder()
                .valid(valid)
                .userId(userId)
                .build();
    }
}
