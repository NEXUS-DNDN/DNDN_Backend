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

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Welfare extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_id", length = 100, nullable = false, unique = true)
    private String servId;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 4000, nullable = false)
    private String content;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    // 상세 링크
    @Column(name = "service_link", length = 1000)
    private String servLink;

    // 시도명
    @Column(name = "ctpv_nm", length = 50, nullable = true)
    private String ctpvNm;

    // 시군구명
    @Column(name = "sgg_nm", length = 50, nullable = true)
    private String sggNm;

    // 대상자 설명
    @Column(name = "eligible_user", length = 1000, nullable = false)
    private String eligibleUser;

    // 제출 서류
    @Column(name = "submit_document", length = 1000, nullable = false)
    private String submitDocument;

    // 신청 시작일
    @Column(name = "start_date", nullable = true)
    private LocalDateTime startDate;

    // 신청 마감일
    @Column(name = "end_date", nullable = true)
    private LocalDateTime endDate;

    // 담당부처
    @Column(name = "department", length = 200, nullable = true)
    private String department;

    // 담당기관(조직)
    @Column(name = "org", length = 200, nullable = true)
    private String org;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnore
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false, length = 20)
    private SourceType sourceType;

    @Builder
    private Welfare(String servId, String title, String content, String servLink,
                    String ctpvNm, String sggNm,
                    String imageUrl, String eligibleUser,
                    String submitDocument, LocalDateTime startDate, LocalDateTime endDate,
                    String department, String org,
                    SourceType sourceType, Category category) {
        this.servId = servId;
        this.title = title;
        this.content = content;
        this.servLink = servLink;
        this.ctpvNm = ctpvNm;
        this.sggNm = sggNm;
        this.imageUrl = imageUrl;
        this.eligibleUser = eligibleUser;
        this.submitDocument = submitDocument;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public void update(String content, String servLink, String eligibleUser, String submitDocument) {
        this.content = content;
        this.servLink = servLink;
        this.eligibleUser = eligibleUser;
        this.submitDocument = submitDocument;
    }

    public void updatePeriod(LocalDateTime start, LocalDateTime end) {
        this.startDate = start;
        this.endDate = end;
    }

}
