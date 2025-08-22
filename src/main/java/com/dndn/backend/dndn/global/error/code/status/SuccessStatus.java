package com.dndn.backend.dndn.global.error.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode{

    // Common
    OK(HttpStatus.OK, "COMMON_200", "성공입니다."),

    // Login
    SUCCESS_LOGIN(HttpStatus.OK, "AUTH_200", "소셜 로그인이 완료 되었습니다."),
    SUCCESS_LOGOUT(HttpStatus.OK, "AUTH_200", "로그 아웃이 완료 되었습니다."),
    SUCCESS_WITHDRAW(HttpStatus.NO_CONTENT, "AUTH_204", "회원 탈퇴가 완료 되었습니다."),
    SUCCESS_TOKEN_REFRESH(HttpStatus.OK, "TOKEN_200", "토큰 재발급 성공"),

    // Interest
    INTEREST_UPDATED(HttpStatus.OK, "INTEREST_200", "관심 상태가 업데이트되었습니다."),

    // Application
    APPLICATION_CREATED(HttpStatus.CREATED, "APPLICATION_201", "신청이 완료 되었습니다."),
    APPLICATION_RECEIVED_UPDATED(HttpStatus.NO_CONTENT, "APPLICATION_204", "수령 상태 업데이트 성공");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build();
    }
}
