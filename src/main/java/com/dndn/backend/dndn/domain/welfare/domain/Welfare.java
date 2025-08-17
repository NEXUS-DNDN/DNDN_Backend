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

    @Column(name = "service_id", nullable = false, unique = true)
    private String servId;

    @Column(nullable = false)
    private String title;

    // 본문은 TEXT/CLOB로
    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "image_url")
    private String imageUrl;

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

    // 제출 서류
    @Lob
    @Column(name = "submit_document", nullable = false, columnDefinition = "TEXT")
    private String submitDocument;

    // 신청 시작일
    @Column(name = "start_date", nullable = true)
    private LocalDateTime startDate;

    // 신청 마감일
    @Column(name = "end_date", nullable = true)
    private LocalDateTime endDate;

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
