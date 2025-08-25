package com.dndn.backend.dndn.domain.user.api;

import com.dndn.backend.dndn.domain.model.enums.IncomeRange;
import com.dndn.backend.dndn.domain.user.application.GooGleOAuthClient;
import com.dndn.backend.dndn.domain.user.application.KakaoOAuthClient;
import com.dndn.backend.dndn.domain.user.application.NaverOAuthClient;
import com.dndn.backend.dndn.domain.user.dto.AuthResponseDTO;
import com.dndn.backend.dndn.domain.model.enums.GenderType;
import com.dndn.backend.dndn.domain.user.domain.entity.User;
import com.dndn.backend.dndn.domain.user.domain.repository.UserRepository;
import com.dndn.backend.dndn.global.config.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebTestController {

    private final KakaoOAuthClient kakaoOAuthClient;
    private final NaverOAuthClient naverOAuthClient;
    private final GooGleOAuthClient googleOAuthClient;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Value("${spring.oauth.kakao.js-key}")
    private String kakaoApiKey;

    @Value("${spring.oauth.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${spring.oauth.naver.client-id}")
    private String naverClientId;

    @Value("${spring.oauth.naver.redirect-uri}")
    private String naverRedirectUri;

    @Value("${spring.oauth.google.client-id}")
    private String googleClientId;

    @Value("${spring.oauth.google.redirect-uri}")
    private String googleRedirectUri;

    @GetMapping("/")
    public String loginPage(Model model) {
        model.addAttribute("kakaoApiKey", kakaoApiKey);
        model.addAttribute("kakaoRedirectUri", kakaoRedirectUri);

        model.addAttribute("naverClientId", naverClientId);
        model.addAttribute("naverRedirectUri", naverRedirectUri);

        model.addAttribute("googleClientId", googleClientId);
        model.addAttribute("googleRedirectUri", googleRedirectUri);

        return "index";
    }

    @GetMapping("/login/oauth/kakao")
    public String kakaoCallback(@RequestParam String code, Model model) {
        // 1. 카카오 토큰 및 사용자 정보 요청
        AuthResponseDTO.KakaoToken kakaoToken = kakaoOAuthClient.requestToken(code);
        AuthResponseDTO.KakaoUserInfo userInfo = kakaoOAuthClient.requestUserInfo(kakaoToken.getAccessToken());

        // 2. Optional 기반 null-safe 파싱
        Optional<AuthResponseDTO.KakaoUserInfo.KakaoAccount> kakaoAccountOpt = Optional.ofNullable(userInfo.getKakaoAccount());

        String nickname = kakaoAccountOpt
                .map(AuthResponseDTO.KakaoUserInfo.KakaoAccount::getProfile)
                .map(AuthResponseDTO.KakaoUserInfo.KakaoAccount.Profile::getNickname)
                .orElse("게스트");

        String profileImageUrl = kakaoAccountOpt
                .map(AuthResponseDTO.KakaoUserInfo.KakaoAccount::getProfile)
                .map(AuthResponseDTO.KakaoUserInfo.KakaoAccount.Profile::getProfileImageUrl)
                .orElse(null);

        // 3. 디버깅 로그로 확인
        log.info("✅ [카카오 로그인 응답] id={}, nickname={}, profileImageUrl={}",
                userInfo.getId(), nickname, profileImageUrl);

        // 4. 기존 사용자 조회 또는 신규 저장
        User user = userRepository.findBySocialId(String.valueOf(userInfo.getId()))
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .socialId(String.valueOf(userInfo.getId()))
                                .profileImageUrl(profileImageUrl)
                                .name("미입력")
                                .phoneNumber("미입력")
                                .birthday(null)
                                .address("미입력")
                                .householdNumber(0)
                                .monthlyIncome(IncomeRange.UNDER_100)
                                .gender(GenderType.UNKNOWN)
                                .build()
                ));

        // 5. JWT 발급
        String jwtAccessToken = jwtUtil.generateAccessToken(String.valueOf(user.getId()));
        String jwtRefreshToken = jwtUtil.generateRefreshToken(String.valueOf(user.getId()));

        // 6. 모델에 전달
        model.addAttribute("authCode", code);
        model.addAttribute("kakaoAccessToken", kakaoToken.getAccessToken());
        model.addAttribute("kakaoRefreshToken", kakaoToken.getRefreshToken());
        model.addAttribute("accessToken", jwtAccessToken);
        model.addAttribute("refreshToken", jwtRefreshToken);

        return "kakaoCallback";
    }

    @GetMapping("/login/oauth/naver")
    public String naverCallBack(
            @RequestParam String code,
            @RequestParam String state,
            Model model
    ) {
        // ✅ code/state만 모델에 담아서 뷰에 전달
        log.info("네이버 로그인 code={}, state={}", code, state);

        model.addAttribute("authCode", code);
        model.addAttribute("state", state);

        // code/state만 보여주는 뷰
        return "naverCallBack"; //
    }



    @GetMapping("/login/oauth/google")
    public String googleCallback(@RequestParam String code, Model model) {
        // 1. 구글 토큰 요청
        AuthResponseDTO.GoogleToken googleToken = googleOAuthClient.requestToken(code);

        // 2. 사용자 정보 요청
        AuthResponseDTO.GoogleUserInfo userInfo = googleOAuthClient.requestUserInfo(googleToken.getAccessToken());

        String nickname = Optional.ofNullable(userInfo.getName()).orElse("게스트");
        String profileImageUrl = userInfo.getProfileImage();

        log.info("✅ [구글 로그인 응답] id={}, nickname={}, profileImage={}", userInfo.getId(), nickname, profileImageUrl);

        // 3. 기존 사용자 확인 또는 신규 저장
        User user = userRepository.findBySocialId(userInfo.getId())
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .socialId(userInfo.getId())
                                .profileImageUrl(profileImageUrl)
                                .name("미입력")
                                .phoneNumber("미입력")
                                .birthday(null)
                                .address("미입력")
                                .householdNumber(0)
                                .monthlyIncome(IncomeRange.UNDER_100)
                                .gender(GenderType.UNKNOWN)
                                .build()
                ));

        // 4. JWT 발급
        String jwtAccessToken = jwtUtil.generateAccessToken(String.valueOf(user.getId()));
        String jwtRefreshToken = jwtUtil.generateRefreshToken(String.valueOf(user.getId()));

        // 5. 모델 전달
        model.addAttribute("authCode", code);
        model.addAttribute("googleAccessToken", googleToken.getAccessToken());
        model.addAttribute("googleRefreshToken", googleToken.getRefreshToken());
        model.addAttribute("accessToken", jwtAccessToken);
        model.addAttribute("refreshToken", jwtRefreshToken);

        return "googleCallback"; // googleCallback.html
    }


}
