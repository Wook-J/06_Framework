package edu.kh.project.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import edu.kh.project.security.handler.JwtAuthenticationEntryPoint;
import edu.kh.project.security.jwt.JwtRequestFilter;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtRequestFilter jwtRequestFilter;
    
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }
    
    @Bean
    public SecurityFilterChain config(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        MvcRequestMatcher[] PERMIT_ALL_WHITE_LIST = {
                mvc.pattern("/store"),
                mvc.pattern("/manage/store/admin"),
                mvc.pattern("/manage/login")
        };

        http.csrf(AbstractHttpConfigurer::disable);

        http.headers((headers) ->
                headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        // http request 인증 설정
        http.authorizeHttpRequests(authorize ->
                authorize.requestMatchers(PERMIT_ALL_WHITE_LIST).permitAll()
                        .anyRequest().authenticated()
        );

        // 인증 실패 시 exception handler 설정
        http.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(jwtAuthenticationEntryPoint));

        // Spring Security에서 session을 생성하거나 사용하지 않도록 설정
        http.sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // jwt Filter 적용
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
