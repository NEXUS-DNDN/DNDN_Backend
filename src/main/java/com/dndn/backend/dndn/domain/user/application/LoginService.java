package com.dndn.backend.dndn.domain.user.application;

import com.dndn.backend.dndn.domain.user.dto.AuthResponseDTO;

public interface LoginService {

    // KAKAO 웹 로그인
    AuthResponseDTO.LoginResult kakaoLogin(String code);

    // KAKAO 앱 로그인
    AuthResponseDTO.LoginResult kakaoLoginWithAccessToken(String kakaoAccessToken);

    // 네이버 웹 로그인
    AuthResponseDTO.LoginResult naverLogin(String code);

    // 네이버 앱 로그인
    AuthResponseDTO.LoginResult naverLoginWithAccessToken(String naverAccessToken);

    // 구글 웹 로그인
    AuthResponseDTO.LoginResult googleLogin(String code);

    // 구글 앱 로그인
    AuthResponseDTO.LoginResult googleLoginWithAccessToken(String googleAccessToken);

    // 토큰 재발급
    AuthResponseDTO.LoginResult refreshToken(Long userId, String refreshToken);
}
