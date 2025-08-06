package com.dndn.backend.dndn.domain.user.service;


import com.dndn.backend.dndn.domain.model.enums.AdditionalInformation;
import com.dndn.backend.dndn.domain.model.exception.UserException;
import com.dndn.backend.dndn.domain.user.domain.entity.Disabled;
import com.dndn.backend.dndn.domain.user.domain.entity.Senior;
import com.dndn.backend.dndn.domain.user.domain.entity.User;
import com.dndn.backend.dndn.domain.user.dto.DisabledRequestDTO;
import com.dndn.backend.dndn.domain.user.dto.SeniorRequestDTO;
import com.dndn.backend.dndn.domain.user.dto.UserRequestDTO;
import com.dndn.backend.dndn.domain.user.repository.UserRepository;
import com.dndn.backend.dndn.global.error.code.status.ErrorStatus;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .additionalInformation(dto.getAdditionalInformation())
                .build();

        userRepository.save(user);

        if (dto.getAdditionalInformation() == AdditionalInformation.SENIOR) {
            registerSeniorInfo(user.getId(), dto.getSeniorInfo());
        } else if (dto.getAdditionalInformation() == AdditionalInformation.DISABLED) {
            registerDisabledInfo(user.getId(), dto.getDisabledInfo());
        }

        return user;
    }


    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorStatus._USER_NOT_FOUND));
    }

    public Senior registerSeniorInfo(Long userId, SeniorRequestDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorStatus._USER_NOT_FOUND));

        if (user.getAdditionalInformation() != AdditionalInformation.SENIOR) {
            throw new UserException(ErrorStatus._INVALID_ADDITIONAL_INFO);
        }

        Senior info = Senior.builder()
                .livingWithChildren(dto.isLivingWithChildren())
                .houseHolder(dto.isHouseHolder())
                .build();

        user.setSeniorInfo(info); // 연관관계 메서드
        userRepository.save(user);// cascade로 info도 같이 저장됨
        return info;
    }

    public Disabled registerDisabledInfo(Long userId, DisabledRequestDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorStatus._USER_NOT_FOUND));

        if (user.getAdditionalInformation() != AdditionalInformation.DISABLED) {
            throw new UserException(ErrorStatus._INVALID_ADDITIONAL_INFO);
        }

        Disabled info = Disabled.builder()
                .disabillityGrade(dto.getDisabillityGrade())
                .disabilityType(dto.getDisabilityType())
                .build();

        user.setDisabledInfo(info); // 연관관계 메서드
        userRepository.save(user);       // cascade로 info도 같이 저장됨
        return info;
    }


//    public Senior getSeniorInfo(Long userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new UserNotFoundException());
//
//        return seniorRepository.findByUser(user)
//                .orElseThrow(() -> new SeniorInfoNotFoundException());
//    }
//
//    public Disabled getDisabledInfo(Long userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new UserNotFoundException());
//
//        return disabledRepository.findByUser(user)
//                .orElseThrow(() -> new DisabledInfoNotFoundException());
//    }
//



}
