package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Используем BCrypt для хэширования паролей
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Отключаем CSRF для упрощения (в продакшене используйте с осторожностью)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN") // Только администраторы
                .requestMatchers("/doctor/**").hasAnyRole("ADMIN", "DOCTOR") // Администраторы и врачи
                .requestMatchers("/user/**").hasAnyRole("ADMIN", "DOCTOR", "USER") // Все три роли
                .requestMatchers("/register", "/").permitAll()  // Разрешаем доступ к регистрации и главной странице
                .requestMatchers("/login").permitAll() // Разрешаем доступ к странице логина
                .anyRequest().authenticated()  // Все остальные запросы требуют аутентификации
            )
            .formLogin(form -> form
                .loginPage("/login") // Указываем "/login" как страницу входа
                .failureUrl("/login?error") // При неудаче перенаправляем на "/login?error"
                .successHandler(customAuthenticationSuccessHandler()) // Обработчик успешной аутентификации
                .permitAll() // Разрешаем доступ к форме логина для всех
            )
            .logout(logout -> logout
                .logoutUrl("/logout") // URL для выхода
                .logoutSuccessUrl("/?logout") // Успешный выход перенаправляет на главную страницу
                .permitAll() // Разрешаем доступ к выходу для всех
            );

        return http.build(); // Создаем цепочку безопасности
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            var authorities = authentication.getAuthorities();

            if (authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                response.sendRedirect("/dashboard/admin"); // Перенаправление для админа
            } else if (authorities.contains(new SimpleGrantedAuthority("ROLE_DOCTOR"))) {
                response.sendRedirect("/dashboard/doctor"); // Перенаправление для врача
            } else if (authorities.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
                response.sendRedirect("/dashboard/user"); // Перенаправление для пользователя
            } else {
                response.sendRedirect("/?error"); // Перенаправление при ошибке
            }
        };
    }
}
