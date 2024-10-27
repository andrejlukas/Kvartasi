package com.mojkvart.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("com.mojkvart.entities")
@EnableJpaRepositories("com.mojkvart.repos")
@EnableTransactionManagement
public class DomainConfig {
}
