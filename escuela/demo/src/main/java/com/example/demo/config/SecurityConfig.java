package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF is enabled by default in Spring Security 6.
                // We keep it enabled for better security on forms.
                .authorizeHttpRequests(authz -> authz
                        // Static resources and public endpoints
                        .requestMatchers("/", "/index", "/login", "/registro/**",
                                "/corporativo", "/servicios-eventos",
                                "/clases-publicas", "/pagos/checkout/**",
                                "/contacto", "/testimonio/nuevo")
                        .permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**")
                        .permitAll()

                        // Role based access
                        .requestMatchers("/panel/admin/**", "/pagos/**", "/estudiantes/**",
                                "/horarios/**")
                        .hasRole("ADMIN")
                        .requestMatchers("/panel/estudiante/**")
                        .hasAnyRole("ESTUDIANTE", "ADMIN")
                        .requestMatchers("/panel/instructor/**")
                        .hasAnyRole("INSTRUCTOR", "ADMIN")

                        // Everything else requires authentication
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/login/success", true)
                        .failureUrl("/login?error=true")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

