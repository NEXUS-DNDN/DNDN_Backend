package com.dndn.backend.dndn.domain.application.domain;

import com.dndn.backend.dndn.domain.application.domain.enums.ReceiveStatus;
import com.dndn.backend.dndn.domain.model.entity.BaseEntity;
import com.dndn.backend.dndn.domain.user.domain.entity.User;
import com.dndn.backend.dndn.domain.welfare.domain.Welfare;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Application extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "welfare_id", nullable = false)
    private Welfare welfare;

    // 수령 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReceiveStatus receiveStatus; // NOT_RECEIVED or RECEIVED

    @Column(nullable = false)
    private LocalDate appliedAt;

    @Builder
    public Application(User user, Welfare welfare, LocalDate appliedAt) {
        this.user = user;
        this.welfare = welfare;
        this.appliedAt = appliedAt;
        this.receiveStatus = ReceiveStatus.NOT_RECEIVED;
    }

    // 수령 완료로 업데이트
    public void updateReceiveStatus() {
        this.receiveStatus = ReceiveStatus.RECEIVED;
    }
}

