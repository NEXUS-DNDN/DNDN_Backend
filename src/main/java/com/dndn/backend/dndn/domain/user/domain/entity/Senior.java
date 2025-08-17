package com.dndn.backend.dndn.domain.user.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Senior {

    @Id @GeneratedValue
    private int id;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;

    private boolean livingWithChildren;

    private boolean isReceivingBasicPension;

    public void registerUser(User user) {
        this.user = user;
    }
}
