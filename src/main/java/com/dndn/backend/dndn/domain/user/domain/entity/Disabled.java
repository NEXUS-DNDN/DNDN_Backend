package com.dndn.backend.dndn.domain.user.domain.entity;


import com.dndn.backend.dndn.domain.model.enums.DisabilityType;
import jakarta.persistence.*;

@Entity
public class Disabled {

    @Id
    @GeneratedValue
    @Column(name="disabled_id")
    private int id;

    @OneToOne(mappedBy = "disabled")
    private User user;

    private int disabillityGrade;

    private DisabilityType disabilityType;

}
