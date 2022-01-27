package com.ssafy.backend.domain.telestation.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TelestationRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer telestationRuleId;

    @Column(nullable = false)
    private Integer telestationPersonnelMin;

    @Column(nullable = false)
    private Integer telestationPersonnelMax;

    private Integer telestationArtTime;

    private Integer telestationTextTime;
}
