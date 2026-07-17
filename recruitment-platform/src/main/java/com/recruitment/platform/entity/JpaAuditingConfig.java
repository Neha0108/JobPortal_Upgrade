package com.recruitment.platform.entity;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/** Activates @CreatedDate/@LastModifiedDate handling declared on BaseEntity. */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}