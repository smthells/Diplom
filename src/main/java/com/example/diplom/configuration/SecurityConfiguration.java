package com.example.diplom.configuration;


import com.example.diplom.service.JwtFilterService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtFilterService jwtFilterService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .requestMatchers()
                .antMatchers("/signup")
                .and()
                .cors().and().csrf().disable()
                .authorizeRequests().antMatchers("/signup").permitAll()
                .and()
                .requestMatchers()
                .antMatchers("/login")
                .and()
                .cors().and().csrf().disable()
                .authorizeRequests().antMatchers("/login").permitAll()
                .and()
                .logout(logout -> logout
                        .logoutUrl("/login?logout")
                        .clearAuthentication(true)
                        .deleteCookies()
                        .invalidateHttpSession(true))
                .requestMatchers()
                .anyRequest()
                .and()
                .cors().and().csrf().disable()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtFilterService, UsernamePasswordAuthenticationFilter.class).build();
    }
}