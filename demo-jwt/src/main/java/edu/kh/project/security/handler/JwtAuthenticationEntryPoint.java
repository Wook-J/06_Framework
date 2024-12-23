package edu.kh.project.security.handler;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint{

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		log.info("[JwtAuthenticationEntryPoint] 토큰 정보가 만료되었거나 존재하지 않습니다.");
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UnAuthorized");
		
	}
}
