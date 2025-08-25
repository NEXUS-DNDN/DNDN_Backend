package com.dndn.backend.dndn.domain.welfare.domain;

import com.dndn.backend.dndn.domain.category.domain.Category;
import com.dndn.backend.dndn.domain.model.entity.BaseEntity;
import com.dndn.backend.dndn.domain.welfare.domain.enums.SourceType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Welfare extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_id", nullable = false, unique = true)
    private String servId;

    @Column(nullable = false)
    private String title;

    // 본문은 TEXT/CLOB로
    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // 상세 링크
    @Column(name = "service_link")
    private String servLink;

    // 시도명
    @Column(name = "ctpv_nm", nullable = true)
    private String ctpvNm;

    // 시군구명
    @Column(name = "sgg_nm", nullable = true)
    private String sggNm;

    // 대상자 설명
    @Lob
    @Column(name = "eligible_user", nullable = false, columnDefinition = "TEXT")
    private String eligibleUser;

    // 상세 정보
    @Column(name = "detail_info", length = 1000)
    private String detailInfo;

    // 담당부처
    @Column(name = "department", length = 200, nullable = true)
    private String department;

    // 담당기관(조직)
    @Column(name = "org", length = 200, nullable = true)
    private String org;

    // 요약 정보
    @Column(name = "summary", length = 1000)
    private String summary;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnore
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false, length = 20)
    private SourceType sourceType;

    @Builder
    private Welfare(String servId, String title, String summary , String content, String servLink,
                    String ctpvNm, String sggNm, String eligibleUser,
                    String detailInfo, String department, String org,
                    SourceType sourceType, Category category) {
        this.servId = servId;
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.servLink = servLink;
        this.detailInfo = detailInfo;
        this.ctpvNm = ctpvNm;
        this.sggNm = sggNm;
        this.eligibleUser = eligibleUser;
        this.department = department;
        this.org = org;
        this.sourceType = sourceType;
        this.category = category;
    }

    public void updateCategory(Category category) {
        this.category = category;
    }

    public void updateRegion(String ctpvNm, String sggNm) {
        this.ctpvNm = ctpvNm;
        this.sggNm = sggNm;
    }

    public void update(String summary, String content, String servLink,
                       String department, String org,
                       String eligibleUser, String detailInfo) {
        this.summary = summary;
        this.content = content;
        this.servLink = servLink;
        this.department = department;
        this.org = org;
        this.eligibleUser = eligibleUser;
        this.detailInfo = detailInfo;
    }

}
