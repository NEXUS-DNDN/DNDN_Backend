package com.dndn.backend.dndn.domain.user.dto;

import com.dndn.backend.dndn.domain.model.enums.*;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserRequestDTO {

    private String name;
    private String phoneNumber;
    private LocalDate birthday;
    private String address;
    private int householdNumber;

    private IncomeRange monthlyIncome;
    private GenderType gender;
    private FamilyType family;
    private EmploymentType employment;
    private AdditionalInformation additionalInformation;

    private SeniorRequestDTO seniorInfo;
    private DisabledRequestDTO disabledInfo;
}
