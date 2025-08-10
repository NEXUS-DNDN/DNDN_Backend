package com.dndn.backend.dndn.domain.user.dto;

import com.dndn.backend.dndn.domain.model.enums.DisabilityType;
import lombok.Getter;

@Getter
public class DisabledRequestDTO {

    private int disabillityGrade;

    private DisabilityType disabilityType;
}
