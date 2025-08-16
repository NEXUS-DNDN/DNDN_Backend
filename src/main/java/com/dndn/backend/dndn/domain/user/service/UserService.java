package com.dndn.backend.dndn.domain.user.service;


import com.dndn.backend.dndn.domain.category.domain.enums.HouseholdType;
import com.dndn.backend.dndn.domain.category.domain.enums.LifeCycle;
import com.dndn.backend.dndn.domain.model.exception.UserException;
import com.dndn.backend.dndn.domain.user.domain.entity.Disabled;
import com.dndn.backend.dndn.domain.user.domain.entity.Senior;
import com.dndn.backend.dndn.domain.user.domain.entity.User;
import com.dndn.backend.dndn.domain.user.domain.repository.UserRepository;
import com.dndn.backend.dndn.domain.user.dto.DisabledRequestDTO;
import com.dndn.backend.dndn.domain.user.dto.SeniorRequestDTO;
import com.dndn.backend.dndn.domain.user.dto.UserRequestDTO;
import com.dndn.backend.dndn.domain.user.dto.UserUpdateRequestDTO;

import com.dndn.backend.dndn.global.error.code.status.ErrorStatus;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.dndn.backend.dndn.domain.category.domain.enums.LifeCycle.SENIOR;


@Service
@Transactional
@Builder
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(UserRequestDTO dto){


        User user = User.builder()
                .name(dto.getName())
                .phoneNumber(dto.getPhoneNumber())
                .birthday(dto.getBirthday())
                .address(dto.getAddress())
                .householdNumber(dto.getHouseholdNumber())
                .monthlyIncome(dto.getMonthlyIncome())
                .gender(dto.getGender())
                .family(dto.getFamily())
                .employment(dto.getEmployment())
                .lifeCycle(dto.getLifeCycle())
                .build();

        user.getHouseholdTypes().addAll(dto.getHouseholdTypes());

        userRepository.save(user);

        return user;
    }


    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorStatus._USER_NOT_FOUND));
    }

    public Senior registerSeniorInfo(Long userId, SeniorRequestDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorStatus._USER_NOT_FOUND));

        if (user.getLifeCycle() != SENIOR) {
            throw new UserException(ErrorStatus._INVALID_ADDITIONAL_INFO);
        }

        Senior info = Senior.builder()
                .livingWithChildren(dto.isLivingWithChildren())
                .isReceivingBasicPension(dto.isReceivingBasicPension())
                .build();

        user.setSeniorInfo(info); // 연관관계 메서드
        userRepository.save(user);// cascade로 info도 같이 저장됨
        return info;
    }

    public Disabled registerDisabledInfo(Long userId, DisabledRequestDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorStatus._USER_NOT_FOUND));

        boolean isDisabled = user.getHouseholdTypes().stream()
                .anyMatch(ht -> ht == HouseholdType.DISABLED);

        if (!isDisabled) {
            throw new UserException(ErrorStatus._INVALID_ADDITIONAL_INFO);
        }

        Disabled info = Disabled.builder()
                .disabillityGrade(dto.getDisabillityGrade())
                .disabilityType(dto.getDisabilityType())
                .registeredDisabled(dto.isRegisteredDisabled())
                .build();

        user.setDisabledInfo(info); // 연관관계 메서드
        userRepository.save(user);       // cascade로 info도 같이 저장됨
        return info;
    }


    public Senior getSeniorInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorStatus._USER_NOT_FOUND));

        if (user.getLifeCycle() != SENIOR) {
            throw new UserException(ErrorStatus._INVALID_ADDITIONAL_INFO);
        }

        return user.getSeniorInfo();
    }

    public Disabled getDisabledInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorStatus._USER_NOT_FOUND));

        boolean isDisabled = user.getHouseholdTypes().stream()
                .anyMatch(ht -> ht == HouseholdType.DISABLED);

        if (!isDisabled) {
            throw new UserException(ErrorStatus._INVALID_ADDITIONAL_INFO);
        }

        return user.getDisabledInfo();
    }

    public User updateUser(Long userId, UserUpdateRequestDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorStatus._USER_NOT_FOUND));

        return user.updateInfo(dto); // 엔티티 안에 update 메서드 정의해둠
    }


}
