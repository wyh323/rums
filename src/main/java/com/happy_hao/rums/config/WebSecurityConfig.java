package com.happy_hao.rums.config;

import com.happy_hao.rums.handler.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/**", "/user/register/**", "/user/login/**", "/user/logout", "/swagger-ui/**", "/swagger-resources/**", "/webjars/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .successHandler(new MyAuthenticationSuccessHandler())
                        .failureHandler(new MyAuthenticationFailureHandler() )
                )
                .logout(logout -> logout
                        .logoutSuccessHandler(new MyLogoutSuccessHandler())
                )
//                .exceptionHandling(exception -> exception
//                        .authenticationEntryPoint(new MyAuthenticationEntryPoint())
//                )
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session
                        .maximumSessions(1).expiredSessionStrategy(new MySessionInformationExpiredStrategy())
                )
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
