package com.dndn.backend.dndn.global.error.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode{

    // Common
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    //User
    _USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER4001", "존재하지 않는 사용자입니다."),
    _INVALID_ADDITIONAL_INFO(HttpStatus.BAD_REQUEST, "USER4002", "추가정보 유형이 일치하지 않습니다."),
    _INVALID_INFO_CHANGE(HttpStatus.BAD_REQUEST,"USER4003","유효하지 않은 사용자 입력입니다."),
    NO_SUCH_LOGIN_TYPE(HttpStatus.BAD_REQUEST, "USER_4004", "지원하지 않는 로그인 타입입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN_4001", "유효하지 않은 refreshToken입니다.");


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
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
