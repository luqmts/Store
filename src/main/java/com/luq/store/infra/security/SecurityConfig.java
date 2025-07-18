package com.luq.store.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    SecurityFilter securityFilter;

    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurity(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
            .securityMatcher("/api/**")
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/product").hasRole("ADMIN")
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/customer", "/api/orders", "/api/products", "/api/sellers", "/api/suppliers", "/api/supply"
                ).hasRole("ADMIN")
                .anyRequest()
                .authenticated()
            )
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain webSecurity(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
            .securityMatcher("/**")
            .csrf(csrf -> csrf.disable())
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
            ).formLogin((form) -> form
                .loginPage("/login")
                .defaultSuccessUrl("/order/list", true)
                .permitAll()
            )
            .logout((logout) -> logout.permitAll())
            .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}