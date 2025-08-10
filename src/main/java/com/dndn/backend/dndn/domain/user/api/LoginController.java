package com.dndn.backend.dndn.domain.user.api;

import com.dndn.backend.dndn.domain.user.application.LoginService;
import com.dndn.backend.dndn.domain.user.dto.AuthRequestDTO;
import com.dndn.backend.dndn.domain.user.dto.AuthResponseDTO;
import com.dndn.backend.dndn.domain.user.exception.UserException;
import com.dndn.backend.dndn.global.common.response.BaseResponse;
import com.dndn.backend.dndn.global.error.code.status.ErrorStatus;
import com.dndn.backend.dndn.global.error.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    // 카카오 accessToken 을 받아 JWT access/refresh 토큰을 발급하고 refreshToken은 Redis에 저장
    @PostMapping("/login/social")
    @Operation(
            summary = "소셜 로그인",
            description = "access token과 로그인 타입을 이용해 JWT 토큰 발급 및 Redis 저장"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH_200", description = "OK, 로그인을 완료했습니다.")
    })
    public BaseResponse<AuthResponseDTO.LoginResult> loginWithAccessToken(@RequestBody AuthRequestDTO.SocialLoginRequest request) {
        AuthResponseDTO.LoginResult result;

        switch (request.getLoginType()) {
            case KAKAO -> result = loginService.kakaoLoginWithAccessToken(request.getAccessToken());
            case NAVER -> result = loginService.naverLoginWithAccessToken(request.getAccessToken());
            case GOOGLE -> result = loginService.googleLoginWithAccessToken(request.getAccessToken());
            default -> throw new UserException(ErrorStatus.NO_SUCH_LOGIN_TYPE);
        }

        return BaseResponse.onSuccess(SuccessStatus.SUCCESS_LOGIN, result);
    }

    @PostMapping("/refreshToken")
    @Operation(
            summary = "토큰 재발급 - refreshToken",
            description = "만료된 accessToken을 대체할 새로운 accessToken과 refreshToken을 발급합니다. \n\n" +
                    "클라이언트는 userId와 refreshToken을 함께 전송해야 하며, 서버는 Redis에 저장된 refreshToken과 일치하는지 검증 후 새로운 JWT를 반환합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "TOKEN_200", description = "토큰 재발급 성공"),
            @ApiResponse(responseCode = "TOKEN_401", description = "유효하지 않거나 만료된 refreshToken"),
            @ApiResponse(responseCode = "TOKEN_404", description = "Redis에 해당 userId의 refreshToken이 존재하지 않음")
    })
    public BaseResponse<AuthResponseDTO.LoginResult> refreshToken(@RequestBody AuthRequestDTO.RefreshToken request) {
        AuthResponseDTO.LoginResult result = loginService.refreshToken(request.getUserId(), request.getRefreshToken());
        return BaseResponse.onSuccess(SuccessStatus.SUCCESS_TOKEN_REFRESH, result);
    }
}
