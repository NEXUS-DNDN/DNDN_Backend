package com.dndn.backend.dndn.domain.member.domain.entity;

import com.dndn.backend.dndn.domain.model.entity.BaseEntity;
import com.dndn.backend.dndn.domain.model.enums.GenderType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@Entity
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이름
    @Column(length = 20)
    private String name;

    // 전화번호
    @Column(length = 20)
    private String phoneNumber;

    // 생년월일
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

//    // 가족 유형
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private FamilyType family;
//
//    // 고용 형태
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private EmploymentType employment;
//
//    // 추가 정보
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private AdditionalInformation additionalInformation;
}
