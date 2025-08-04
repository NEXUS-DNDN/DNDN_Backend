package com.dndn.backend.dndn.domain.user.domain.entity;

import com.dndn.backend.dndn.domain.model.entity.BaseEntity;
import com.dndn.backend.dndn.domain.model.enums.AdditionalInformation;
import com.dndn.backend.dndn.domain.model.enums.EmploymentType;
import com.dndn.backend.dndn.domain.model.enums.FamilyType;
import com.dndn.backend.dndn.domain.model.enums.GenderType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이름
    @Column(length = 20)
    private String name;

    // 전화번호
    @Column(length = 20)
    private String phoneNumber;

    // 생년월일
    @Column(name="birth_date")
    private Date birthday;

    // 주소
    @Column(length = 100)
    private String address;

    // 가구원 수
    private int householdNumber;

    // 월 소득
    private int monthlyIncome;

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

    private String profileUrl;
}
