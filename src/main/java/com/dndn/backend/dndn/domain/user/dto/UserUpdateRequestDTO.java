package com.dndn.backend.dndn.domain.user.dto;

import com.dndn.backend.dndn.domain.model.enums.EmploymentType;
import com.dndn.backend.dndn.domain.model.enums.FamilyType;
import com.dndn.backend.dndn.domain.model.enums.GenderType;
import com.dndn.backend.dndn.domain.model.enums.IncomeRange;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class UserUpdateRequestDTO {

    @NotBlank
    private String name;

    @NotNull
    private LocalDate birthday;

    @NotBlank
    private String address;

    private int householdNumber;

    @NotNull
    private IncomeRange monthlyIncome;

    @NotNull
    private GenderType gender;

    @NotNull
    private FamilyType family;

    @NotNull
    private EmploymentType employment;
}
