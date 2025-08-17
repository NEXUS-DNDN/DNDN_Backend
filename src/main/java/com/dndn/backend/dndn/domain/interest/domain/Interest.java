package com.dndn.backend.dndn.domain.interest.domain;

import com.dndn.backend.dndn.domain.model.entity.BaseEntity;
import com.dndn.backend.dndn.domain.user.domain.entity.User;
import com.dndn.backend.dndn.domain.welfare.domain.Welfare;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
        name = "interest",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "welfare_id"})
)
public class Interest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interest_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "welfare_id", nullable = false)
    private Welfare welfare;

    @Column(nullable = false)
    private Boolean interestStatus;

    @Builder
    public Interest(User user, Welfare welfare, Boolean interestStatus) {
        this.user = user;
        this.welfare = welfare;
        this.interestStatus = interestStatus;
    }

    public void updateStatus(Boolean status) {
        this.interestStatus = status;
    }
}
