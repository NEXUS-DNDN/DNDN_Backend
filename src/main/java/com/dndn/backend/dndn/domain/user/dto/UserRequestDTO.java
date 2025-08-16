package com.dndn.backend.dndn.domain.user.dto;

import com.dndn.backend.dndn.domain.category.domain.enums.HouseholdType;
import com.dndn.backend.dndn.domain.category.domain.enums.LifeCycle;
import com.dndn.backend.dndn.domain.model.enums.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Getter
@NoArgsConstructor
public class UserRequestDTO {

    @NotBlank
    private String name;

    private String phoneNumber;
    private LocalDate birthday;
    private String address;
    private int householdNumber;

    private IncomeRange monthlyIncome;
    private GenderType gender;
    private FamilyType family;
    private EmploymentType employment;

    @NotNull
    private LifeCycle lifeCycle;

    @NotEmpty
    private Set<HouseholdType> householdTypes;

}
