package com.dndn.backend.dndn.domain.user.dto;


import com.dndn.backend.dndn.domain.category.domain.enums.HouseholdType;
import com.dndn.backend.dndn.domain.category.domain.enums.LifeCycle;
import com.dndn.backend.dndn.domain.model.enums.*;
import com.dndn.backend.dndn.domain.user.domain.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Builder
public class UserResponseDTO {

    private Long id;
    private String name;
    private String phoneNumber;
    private String address;
    private LocalDate birthday;
    private int householdNumber;
    private IncomeRange monthlyIncome;
    private GenderType gender;
    private EmploymentType employment;
    private LifeCycle lifeCycle;
    private Set<HouseholdType> householdTypes;


    public static UserResponseDTO from(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .birthday(user.getBirthday())
                .householdNumber(user.getHouseholdNumber())
                .monthlyIncome(user.getMonthlyIncome())
                .gender(user.getGender())
                .employment(user.getEmployment())
                .lifeCycle(user.getLifeCycle())
                .householdTypes(user.getHouseholdTypes())
                .build();
    }

}
