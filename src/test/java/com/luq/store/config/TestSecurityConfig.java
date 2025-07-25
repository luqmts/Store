package com.luq.store.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
@Profile("test")
public class TestSecurityConfig {
    @Bean
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
        .securityMatcher("/**")
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(requests -> requests
            .requestMatchers("/login", "/css/**", "/js/**").permitAll()
            .requestMatchers(
                "/user/**",
                "/customer/form", "/customer/form/**", "/customer/delete/**",
                "/product/form", "/product/form/**", "/product/delete/**",
                "/seller/form", "/seller/form/**", "/seller/delete/**",
                "/supplier/form", "/supplier/form/**", "/supplier/delete/**",
                "/supply/form", "/supply/form/**", "/supply/delete/**"
            ).hasRole("ADMIN")
            .anyRequest()
            .authenticated()
        ).formLogin(AbstractHttpConfigurer::disable
        ).logout(LogoutConfigurer::permitAll)
        .build();
    }
}
