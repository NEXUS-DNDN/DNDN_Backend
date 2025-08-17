package com.dndn.backend.dndn.domain.application.api.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ApplicationCreateReqDto(
        @NotNull(message = "신청 날짜는 필수입니다.")
        LocalDate appliedAt
) {
}
