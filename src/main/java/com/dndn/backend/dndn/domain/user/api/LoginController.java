package com.dndn.backend.dndn.domain.user.api;

import com.dndn.backend.dndn.domain.user.application.LoginService;
import com.dndn.backend.dndn.domain.user.dto.AuthRequestDTO;
import com.dndn.backend.dndn.domain.user.dto.AuthResponseDTO;
import com.dndn.backend.dndn.domain.user.exception.UserException;
import com.dndn.backend.dndn.global.common.response.BaseResponse;
import com.dndn.backend.dndn.global.error.code.status.ErrorStatus;
import com.dndn.backend.dndn.global.error.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // 토큰 재발급
    @PostMapping("/refreshToken")
    @Operation(
            summary = "토큰 재발급 - refreshToken",
            description = "만료된 accessToken을 대체할 새로운 accessToken과 refreshToken을 발급합니다. \n\n" +
                    "클라이언트는 userId와 refreshToken을 함께 전송해야 하며, 서버는 Redis에 저장된 refreshToken과 일치하는지 검증 후 새로운 JWT를 반환합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "TOKEN_200", description = "토큰 재발급 성공"),
            @ApiResponse(responseCode = "TOKEN_4001", description = "유효하지 않거나 만료된 refreshToken"),
            @ApiResponse(responseCode = "TOKEN_4004", description = "Redis에 해당 userId의 refreshToken이 존재하지 않음")
    })
    public BaseResponse<AuthResponseDTO.LoginResult> refreshToken(@RequestBody AuthRequestDTO.RefreshToken request) {
        AuthResponseDTO.LoginResult result = loginService.refreshToken(request.getUserId(), request.getRefreshToken());
        return BaseResponse.onSuccess(SuccessStatus.SUCCESS_TOKEN_REFRESH, result);
    }

    // 자동 로그인
    @GetMapping("/auto-login")
    @Operation(
            summary = "자동 로그인 - accessToken 인증",
            description = "앱 실행 시 저장된 accessToken이 유효한지 확인하여 자동 로그인 수행"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "AUTH_200", description = "자동 로그인 성공"),
            @ApiResponse(responseCode = "TOKEN_4002", description = "유효하지 않거나 잘못된 accessToken입니다."),
            @ApiResponse(responseCode = "USER_4001", description = "유저가 존재하지 않습니다.")
    })
    public BaseResponse<AuthResponseDTO.AutoLoginResult> autoLogin(@RequestHeader("Authorization") String accessTokenHeader) {

        AuthResponseDTO.AutoLoginResult result = loginService.autoLogin(accessTokenHeader);

        return BaseResponse.onSuccess(SuccessStatus.SUCCESS_LOGIN, result);
    }

    // 로그 아웃
    @PostMapping("/logout")
    @Operation(
            summary = "로그아웃",
            description = "accessToken을 기반으로 Redis에서 refreshToken을 제거하여 로그아웃합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "AUTH_200", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "TOKEN_4002", description = "유효하지 않거나 잘못된 accessToken입니다."),
            @ApiResponse(responseCode = "USER_4001", description = "유저가 존재하지 않습니다.")
    })
    public BaseResponse<String> logout(@RequestHeader("Authorization") String accessTokenHeader) {

        loginService.logout(accessTokenHeader);

        return BaseResponse.onSuccess(SuccessStatus.SUCCESS_LOGOUT, "로그아웃 완료");
    }

    // 회원 탈퇴
    @DeleteMapping("/withdraw")
    @Operation(
            summary = "회원 탈퇴",
            description = "accessToken을 기반으로 DB에서 유저를 삭제하고 Redis에서 refreshToken도 제거합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "AUTH_204", description = "회원 탈퇴 성공"),
            @ApiResponse(responseCode = "TOKEN_4002", description = "유효하지 않거나 잘못된 accessToken입니다."),
            @ApiResponse(responseCode = "USER_4001", description = "유저가 존재하지 않습니다.")
    })
    public BaseResponse<String> deleteAccount(@RequestHeader("Authorization") String accessTokenHeader) {

        loginService.deleteAccount(accessTokenHeader);

        return BaseResponse.onSuccess(SuccessStatus.SUCCESS_WITHDRAW, "회원 탈퇴가 완료되었습니다.");
    }

    // 인증 번호 발송
    @PostMapping("/send-code")
    @Operation(
            summary = "휴대폰 인증번호 발송",
            description = "사용자 이름과 휴대폰 번호가 일치하는지 확인 후, 해당 번호로 6자리 인증번호를 발송합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "AUTH_200", description = "OK, 인증번호 발송 완료"),
            @ApiResponse(responseCode = "USER_4001", description = "일치하는 사용자 정보가 없음")
    })
    public BaseResponse<String> sendCode(
            @Parameter(description = "사용자 이름", example = "홍길동") @RequestParam String name,
            @Parameter(description = "휴대폰 번호 (숫자만 입력)", example = "01012345678") @RequestParam String phone) {

        loginService.sendVerificationCode(name, phone);

        return BaseResponse.onSuccess(SuccessStatus.SUCCESS_SEND_AUTHENTICATION_CODE, "");
    }

    // sms 인증 로그인
    @PostMapping("/sms-login")
    @Operation(
            summary = "휴대폰 인증번호 검증 및 로그인",
            description = "사용자가 입력한 인증번호가 올바른 경우 JWT Access/Refresh Token을 발급합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "AUTH_200", description = "OK, 로그인 성공"),
            @ApiResponse(responseCode = "TOKEN_4002", description = "인증번호가 잘못되었거나 만료됨"),
            @ApiResponse(responseCode = "USER_4001", description = "사용자 정보 없음")
    })
    public BaseResponse<AuthResponseDTO.PhoneLoginResult> verifyCode(
            @Parameter(description = "휴대폰 번호 (숫자만 입력)", example = "01012345678") @RequestParam String phone,
            @Parameter(description = "6자리 인증번호", example = "123456") @RequestParam String code) {

        AuthResponseDTO.PhoneLoginResult result = loginService.verifyCodeAndLogin(phone, code);
        return BaseResponse.onSuccess(SuccessStatus.SUCCESS_SMS_LOGIN, result);
    }
}