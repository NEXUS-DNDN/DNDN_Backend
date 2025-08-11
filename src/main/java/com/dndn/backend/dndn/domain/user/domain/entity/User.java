package com.dndn.backend.dndn.domain.user.domain.entity;

import com.dndn.backend.dndn.domain.model.entity.BaseEntity;
import com.dndn.backend.dndn.domain.model.enums.*;
import com.dndn.backend.dndn.domain.user.dto.UserUpdateRequestDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;
import java.time.LocalDate;

@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;

    // 소셜 아이디
    @Column(name = "social_id", nullable = false, unique = true)
    private String socialId;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    // 이름
    @Column(length = 20)
    private String name;

    // 전화번호
    @Column(length = 20)
    private String phoneNumber;

    // 생년월일
    @Column(name="birth_date")
    private LocalDate birthday;

    // 주소
    @Column(length = 100)
    private String address;

    // 가구원 수
    private int householdNumber;

    // 월 소득
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IncomeRange monthlyIncome;

    // 성별
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GenderType gender;

    // 가족 유형
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FamilyType family;

    // 고용 형태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmploymentType employment;

    // 추가 정보
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdditionalInformation additionalInformation;

    // 노인일 경우
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Senior seniorInfo;

    //장애인일 경우
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Disabled disabledInfo;

    //연관관계 관련 메소드
    public void setSeniorInfo(Senior seniorInfo) {
        this.seniorInfo = seniorInfo;
        seniorInfo.registerUser(this);
    }

    public void setDisabledInfo(Disabled disabledInfo) {
        this.disabledInfo = disabledInfo;
        disabledInfo.registerUser(this);
    }

    //프로필 사진
    private String profileUrl;

    public User updateInfo(UserUpdateRequestDTO dto) {
        this.name = dto.getName();
        this.birthday = dto.getBirthday();
        this.address = dto.getAddress();
        this.householdNumber = dto.getHouseholdNumber();
        this.monthlyIncome = dto.getMonthlyIncome();
        this.gender = dto.getGender();
        this.family = dto.getFamily();
        this.employment = dto.getEmployment();

        return this;
    }

}
