package com.dndn.backend.dndn.domain.welfare.domain;

import com.dndn.backend.dndn.domain.model.entity.BaseEntity;
import com.dndn.backend.dndn.domain.welfare.domain.enums.ReceiveStatus;
import com.dndn.backend.dndn.domain.welfare.domain.enums.RequestStatus;
import com.dndn.backend.dndn.domain.welfare.domain.enums.SourceType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "service_link", length = 1000)
    private String servLink;

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

    // 신청 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "request_status", nullable = false, length = 20)
    private RequestStatus requestStatus;

    // 수령 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "receive_status", nullable = false, length = 20)
    private ReceiveStatus receiveStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnore
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false, length = 20)
    private SourceType sourceType;

    @Builder
    private Welfare(String servId, String title, String content, String servLink, String imageUrl, String eligibleUser,
                    String submitDocument, LocalDateTime startDate, LocalDateTime endDate,
                    RequestStatus requestStatus, ReceiveStatus receiveStatus,
                    SourceType sourceType, Category category) {
        this.servId = servId;
        this.title = title;
        this.content = content;
        this.servLink = servLink;
        this.imageUrl = imageUrl;
        this.eligibleUser = eligibleUser;
        this.submitDocument = submitDocument;
        this.startDate = startDate;
        this.endDate = endDate;
        this.requestStatus = requestStatus;
        this.receiveStatus = receiveStatus;
        this.sourceType = sourceType;
        this.category = category;
    }

    public void update(String content, String servLink, String eligibleUser, String submitDocument) {
        this.content = content;
        this.servLink = servLink;
        this.eligibleUser = eligibleUser;
        this.submitDocument = submitDocument;
    }


}
