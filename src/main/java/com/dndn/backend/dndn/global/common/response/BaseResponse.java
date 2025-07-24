package com.dndn.backend.dndn.global.common.response;

import com.dndn.backend.dndn.global.error.code.status.BaseCode;
import com.dndn.backend.dndn.global.error.code.status.BaseErrorCode;
import com.dndn.backend.dndn.global.error.code.status.ErrorStatus;
import com.dndn.backend.dndn.global.error.code.status.SuccessStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "statusCode", "message", "result"})
public class BaseResponse<T> {

    @JsonProperty("isSuccess")
    private boolean isSuccess;

    private String statusCode;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    public static <T> BaseResponse<T> onSuccess(BaseCode code, T result) {
        return new BaseResponse<>(
                true,
                code.getCode(),
                code.getMessage(),
                result);
    }

    public static <T> BaseResponse<T> onFailure(BaseErrorCode code, T result) {
        return new BaseResponse<>(
                false,
                code.getCode(),
                code.getMessage(),
                result);
    }
}
