package com.dndn.backend.dndn.domain.user.domain.entity;

import com.dndn.backend.dndn.domain.model.enums.DisabilityType;
import jakarta.persistence.*;

@Entity
public class Senior {

    @Id @GeneratedValue
    @Column(name="senior_id")
    private int id;

    @OneToOne(mappedBy = "senior")
    private User user;

    private boolean livingWithChildren;

    private boolean houseHolder;
}
