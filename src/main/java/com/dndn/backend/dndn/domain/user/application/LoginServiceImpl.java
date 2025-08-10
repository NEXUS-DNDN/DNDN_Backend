package com.dndn.backend.dndn.domain.user.application;

import com.dndn.backend.dndn.domain.model.enums.IncomeRange;
import com.dndn.backend.dndn.domain.user.converter.LoginConverter;
import com.dndn.backend.dndn.domain.user.dto.AuthResponseDTO;
import com.dndn.backend.dndn.domain.model.enums.GenderType;
import com.dndn.backend.dndn.domain.user.domain.entity.User;
import com.dndn.backend.dndn.domain.user.domain.repository.UserRepository;
import com.dndn.backend.dndn.domain.user.exception.UserException;
import com.dndn.backend.dndn.global.config.security.jwt.JwtUtil;
import com.dndn.backend.dndn.global.error.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final KakaoOAuthClient kakaoOAuthClient;
    private final NaverOAuthClient naverOAuthClient;
    private final GooGleOAuthClient googleOAuthClient;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    // KAKAO 웹 로그인
    @Transactional
    @Override
    public AuthResponseDTO.LoginResult kakaoLogin(String code) {
        // 1. 카카오 accessToken 발급
        AuthResponseDTO.KakaoToken token = kakaoOAuthClient.requestToken(code);

        // 2. accessToken으로 사용자 정보 요청
        AuthResponseDTO.KakaoUserInfo userInfo = kakaoOAuthClient.requestUserInfo(token.getAccessToken());

        // 3. 유저 존재 여부 확인
        User user = userRepository.save(
                User.builder()
                        .socialId(String.valueOf(userInfo.getId()))
                        .profileImageUrl(userInfo.getKakaoAccount().getProfile().getProfileImageUrl())
                        .name("미입력")
                        .phoneNumber("미입력")
                        .birthday(null)
                        .address("미입력")
                        .householdNumber(0)
                        .monthlyIncome(IncomeRange.UNDER_100)
                        .gender(GenderType.UNKNOWN)
                        .build()
        );

        boolean isNewUser = !userRepository.existsBySocialId(String.valueOf(userInfo.getId()));

        // 4. JWT 토큰 발급
        String jwtAccessToken = jwtUtil.generateAccessToken(String.valueOf(user.getId()));
        String jwtRefreshToken = jwtUtil.generateRefreshToken(String.valueOf(user.getId()));
        // Redis에 refreshToken 저장
        redisTemplate.opsForValue().set(
                "refresh:userId:" + user.getId(),
                jwtRefreshToken,
                jwtUtil.getRefreshTokenValidity(),
                TimeUnit.MILLISECONDS
        );

        // 5. 최종 응답 (카카오 access/refresh 토큰도 포함해서 반환)
        return LoginConverter.toKAKAOWebLoginResponse(jwtAccessToken, jwtRefreshToken, isNewUser, token);
    }

    // KAKAO 앱 로그인
    @Transactional
    @Override
    public AuthResponseDTO.LoginResult kakaoLoginWithAccessToken(String kakaoAccessToken) {
        // 1. accessToken으로 사용자 정보 요청
        AuthResponseDTO.KakaoUserInfo userInfo = kakaoOAuthClient.requestUserInfo(kakaoAccessToken);
        String socialId = String.valueOf(userInfo.getId());

        // 2. 유저 존재 여부 먼저 판단
        Optional<User> existingUser = userRepository.findBySocialId(socialId);
        boolean isNewUser = existingUser.isEmpty();

        // 3. 없으면 새로 저장
        User user = existingUser.orElseGet(() -> userRepository.save(
                User.builder()
                        .socialId(socialId)
                        .profileImageUrl(userInfo.getKakaoAccount().getProfile().getProfileImageUrl())
                        .name("미입력")
                        .phoneNumber("미입력")
                        .birthday(null)
                        .address("미입력")
                        .householdNumber(0)
                        .monthlyIncome(IncomeRange.UNDER_100)
                        .gender(GenderType.UNKNOWN)
                        .build()
        ));

        // 5. JWT 토큰 발급
        String accessToken = jwtUtil.generateAccessToken(String.valueOf(user.getId()));
        String refreshToken = jwtUtil.generateRefreshToken(String.valueOf(user.getId()));

        // 6. Redis 저장
        redisTemplate.opsForValue().set(
                "refresh:userId:" + user.getId(),
                refreshToken,
                jwtUtil.getRefreshTokenValidity(),
                TimeUnit.MILLISECONDS
        );

        // 7. 응답 반환
        return LoginConverter.toKAKAOLoginResponse(accessToken, refreshToken, kakaoAccessToken, isNewUser);
    }

    // 네이버 웹 로그인
    @Transactional
    @Override
    public AuthResponseDTO.LoginResult naverLogin(String code) {

        // 1. 네이버 access token 발급
        AuthResponseDTO.NaverToken token = naverOAuthClient.requestToken(code);

        // 2. 사용자 정보 요청
        AuthResponseDTO.NaverUserInfo userInfo = naverOAuthClient.requestUserInfo(token.getAccessToken());

        // 3. 유저 존재 여부 확인
        String socialId = String.valueOf(userInfo.getId());
        Optional<User> existingUser = userRepository.findBySocialId(socialId);
        boolean isNewUser = existingUser.isEmpty();

        // 4. 없으면 새로 저장
        User user = existingUser.orElseGet(() -> userRepository.save(
                User.builder()
                        .socialId(socialId)
                        .profileImageUrl(userInfo.getNaverAccount().getProfile().getProfileImageUrl())
                        .name("미입력")
                        .phoneNumber("미입력")
                        .birthday(null)
                        .address("미입력")
                        .householdNumber(0)
                        .monthlyIncome(IncomeRange.UNDER_100)
                        .gender(GenderType.UNKNOWN)
                        .build()
        ));

        // 5. JWT 토큰 발급
        String accessToken = jwtUtil.generateAccessToken(String.valueOf(user.getId()));
        String refreshToken = jwtUtil.generateRefreshToken(String.valueOf(user.getId()));

        // 6. Redis 저장
        redisTemplate.opsForValue().set(
                "refresh:userId:" + user.getId(),
                refreshToken,
                jwtUtil.getRefreshTokenValidity(),
                TimeUnit.MILLISECONDS
        );

        // 7. 응답 반환
        return LoginConverter.toNAVERWebLoginResponse(accessToken, refreshToken, isNewUser, token);
    }

    // 네이버 앱 로그인
    @Transactional
    @Override
    public AuthResponseDTO.LoginResult naverLoginWithAccessToken(String naverAccessToken) {
        // 1. access token으로 네이버 사용자 정보 요청
        AuthResponseDTO.NaverUserInfo userInfo = naverOAuthClient.requestUserInfo(naverAccessToken);
        String socialId = String.valueOf(userInfo.getId());

        // 2. 유저 존재 여부 판단
        Optional<User> existingUser = userRepository.findBySocialId(socialId);
        boolean isNewUser = existingUser.isEmpty();

        // 3. 없으면 새로 저장
        User user = existingUser.orElseGet(() -> userRepository.save(
                User.builder()
                        .socialId(socialId)
                        .profileImageUrl(userInfo.getNaverAccount().getProfile().getProfileImageUrl())
                        .name("미입력")
                        .phoneNumber("미입력")
                        .birthday(null)
                        .address("미입력")
                        .householdNumber(0)
                        .monthlyIncome(IncomeRange.UNDER_100)
                        .gender(GenderType.UNKNOWN)
                        .build()
        ));

        // 4. JWT 토큰 발급
        String accessToken = jwtUtil.generateAccessToken(String.valueOf(user.getId()));
        String refreshToken = jwtUtil.generateRefreshToken(String.valueOf(user.getId()));

        // 5. Redis에 refresh token 저장
        redisTemplate.opsForValue().set(
                "refresh:userId:" + user.getId(),
                refreshToken,
                jwtUtil.getRefreshTokenValidity(),
                TimeUnit.MILLISECONDS
        );

        // 6. 응답 반환
        return LoginConverter.toNAVERLoginResponse(accessToken, refreshToken, naverAccessToken, isNewUser);
    }

    // ✅ 구글 웹 로그인
    @Transactional
    @Override
    public AuthResponseDTO.LoginResult googleLogin(String code) {
        // 1. 구글 access token 발급
        AuthResponseDTO.GoogleToken token = googleOAuthClient.requestToken(code);

        // 2. 사용자 정보 요청
        AuthResponseDTO.GoogleUserInfo userInfo = googleOAuthClient.requestUserInfo(token.getAccessToken());

        // 3. 유저 존재 여부 확인
        String socialId = String.valueOf(userInfo.getId());
        Optional<User> existingUser = userRepository.findBySocialId(socialId);
        boolean isNewUser = existingUser.isEmpty();

        // 4. 없으면 새로 저장
        User user = existingUser.orElseGet(() -> userRepository.save(
                User.builder()
                        .socialId(socialId)
                        .profileImageUrl(userInfo.getGoogleAccount().getProfile().getProfileImageUrl())
                        .name("미입력")
                        .phoneNumber("미입력")
                        .birthday(null)
                        .address("미입력")
                        .householdNumber(0)
                        .monthlyIncome(IncomeRange.UNDER_100)
                        .gender(GenderType.UNKNOWN)
                        .build()
        ));

        // 5. JWT 토큰 발급
        String accessToken = jwtUtil.generateAccessToken(String.valueOf(user.getId()));
        String refreshToken = jwtUtil.generateRefreshToken(String.valueOf(user.getId()));

        // 6. Redis 저장
        redisTemplate.opsForValue().set(
                "refresh:userId:" + user.getId(),
                refreshToken,
                jwtUtil.getRefreshTokenValidity(),
                TimeUnit.MILLISECONDS
        );

        // 7. 응답 반환
        return LoginConverter.toGOOGLEWebLoginResponse(accessToken, refreshToken, isNewUser, token);
    }

    // ✅ 구글 앱 로그인
    @Transactional
    @Override
    public AuthResponseDTO.LoginResult googleLoginWithAccessToken(String googleAccessToken) {
        // 1. access token으로 구글 사용자 정보 요청
        AuthResponseDTO.GoogleUserInfo userInfo = googleOAuthClient.requestUserInfo(googleAccessToken);
        String socialId = String.valueOf(userInfo.getId());

        // 2. 유저 존재 여부 판단
        Optional<User> existingUser = userRepository.findBySocialId(socialId);
        boolean isNewUser = existingUser.isEmpty();

        // 3. 없으면 새로 저장
        User user = existingUser.orElseGet(() -> userRepository.save(
                User.builder()
                        .socialId(socialId)
                        .profileImageUrl(userInfo.getGoogleAccount().getProfile().getProfileImageUrl())
                        .name("미입력")
                        .phoneNumber("미입력")
                        .birthday(null)
                        .address("미입력")
                        .householdNumber(0)
                        .monthlyIncome(IncomeRange.UNDER_100)
                        .gender(GenderType.UNKNOWN)
                        .build()
        ));

        // 4. JWT 토큰 발급
        String accessToken = jwtUtil.generateAccessToken(String.valueOf(user.getId()));
        String refreshToken = jwtUtil.generateRefreshToken(String.valueOf(user.getId()));

        // 5. Redis에 refresh token 저장
        redisTemplate.opsForValue().set(
                "refresh:userId:" + user.getId(),
                refreshToken,
                jwtUtil.getRefreshTokenValidity(),
                TimeUnit.MILLISECONDS
        );

        // 6. 응답 반환
        return LoginConverter.toGOOGLELoginResponse(accessToken, refreshToken, googleAccessToken, isNewUser);
    }


    // 토큰 재발급
    @Override
    public AuthResponseDTO.LoginResult refreshToken(Long userId, String refreshToken) {
        String redisKey = "refresh:userId:" + userId;
        String storedRefreshToken = redisTemplate.opsForValue().get(redisKey);

        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new UserException(ErrorStatus.INVALID_REFRESH_TOKEN);
        }

        // 새 JWT 토큰 생성
        String newAccessToken = jwtUtil.generateAccessToken(String.valueOf(userId));
        String newRefreshToken = jwtUtil.generateRefreshToken(String.valueOf(userId));

        // Redis에 새 refresh 토큰 갱신
        redisTemplate.opsForValue().set(redisKey, newRefreshToken, jwtUtil.getRefreshTokenValidity(), TimeUnit.MILLISECONDS);

        return LoginConverter.toRefreshTokenResponse(newAccessToken, newRefreshToken);
    }
}