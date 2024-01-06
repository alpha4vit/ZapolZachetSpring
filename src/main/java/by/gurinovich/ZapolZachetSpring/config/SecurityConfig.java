package by.gurinovich.ZapolZachetSpring.config;

import by.gurinovich.ZapolZachetSpring.models.User;
import by.gurinovich.ZapolZachetSpring.services.UserService;
import by.gurinovich.ZapolZachetSpring.services.auth.UserDetailsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    public SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/teacher/**").hasAnyRole("TEACHER", "ADMIN")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/auth")
                .loginProcessingUrl("/process_login")
                .successHandler((request, response, authentication) -> {
                    User user = (User) authentication.getPrincipal();
                    switch (user.getRole()) {
                        case "ROLE_USER" -> response.sendRedirect("/choosegroup");
                        case "ROLE_TEACHER" ->  response.sendRedirect("/teacher");
                        case "ROLE_ADMIN" -> response.sendRedirect("/admin/users");
                    }
                })
                .failureHandler((request, response, exception) -> {
                    System.out.println(exception.getMessage());
                    response.sendRedirect("/auth/login/error");
                })
                .and()
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            System.out.println("Access Denied.\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                            if (user != null){
                                if (!user.isEmailVerified()) {
                                    response.sendRedirect("/auth/confirm");
                                    return;
                                }
                            }
                            response.sendRedirect("/auth");
                        }))
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/auth");

        return http.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}