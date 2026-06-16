package com.jobportal.Entities;

import jakarta.persistence.*;

@Entity
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long skillId;

    @Column(unique = true, nullable = false)
    private String skillName;
}
