package com.dndn.backend.dndn.domain.user.domain.entity;


import com.dndn.backend.dndn.domain.model.enums.DisabilityType;
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
public class Disabled {

    @Id
    @GeneratedValue
    private int id;

    @OneToOne
    @JoinColumn(name="user_id")
    private User user;

    private int disabillityGrade;

    @Enumerated(EnumType.STRING)
    private DisabilityType disabilityType;

    public void registerUser(User user) {
        this.user = user;
    }

}
