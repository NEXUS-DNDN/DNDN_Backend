package com.dndn.backend.dndn.domain.user.application;

import com.dndn.backend.dndn.domain.user.dto.AuthResponseDTO;

public interface LoginService {

    // KAKAO 로그인
    AuthResponseDTO.LoginResult kakaoLoginWithAccessToken(String kakaoAccessToken);

    // 네이버 로그인
    AuthResponseDTO.LoginResult naverLoginWithAccessToken(String naverAccessToken);

    // 구글 로그인
    AuthResponseDTO.LoginResult googleLoginWithAccessToken(String googleAccessToken);

    // 토큰 재발급
    AuthResponseDTO.LoginResult refreshToken(Long userId, String refreshToken);

    // 자동 로그인
    AuthResponseDTO.AutoLoginResult autoLogin(String accessToken);

    // 로그 아웃
    void logout(String accessTokenHeader);

    // 회원 탈퇴
    void deleteAccount(String accessToken);

    // sms 인증
    void sendVerificationCode(String name, String phone);

    // sms 로그인
    AuthResponseDTO.PhoneLoginResult verifyCodeAndLogin(String phone, String code);
}