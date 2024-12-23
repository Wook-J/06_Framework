package edu.kh.project.security.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import edu.kh.project.model.dto.AdminDTO;
import edu.kh.project.model.service.AdminService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter{
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private AdminService adminService;
	@Autowired private JwtTokenProvider jwtTokenProvider;
	
	// 포함하지 않을 url
	private static final List<String> EXCLUDE_URL =
		Collections.unmodifiableList(Arrays.asList(
				"/static/**", "/favicon.ico", "/admin",	"/admin/authentication",
				"/admin/refresh", "/admin/join", "/admin/join/**",
				"/admin/loginView", "/admin/login"));

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// jwt cookie 사용 시
		String token = Arrays.stream(request.getCookies())
				.filter(c -> c.getName().equals("jdhToken"))
				.findFirst() .map(Cookie::getValue)
				.orElse(null);
		
		String adminId = null;
		String jwtToken = null;
		
		// Bearer token인 경우
		if (token != null && token.startsWith("Bearer ")) {
			jwtToken = token.substring(7);
			try {
				adminId = jwtTokenProvider.getUsernameFromToken(jwtToken);
				
			} catch (SignatureException e) {
				log.error("Invalid JWT signature: {}", e.getMessage());
			} catch (MalformedJwtException e) {
				log.error("Invalid JWT token: {}", e.getMessage());
			} catch (ExpiredJwtException e) {
				log.error("JWT token is expired: {}", e.getMessage());
			} catch (UnsupportedJwtException e) {
				log.error("JWT token is unsupported: {}", e.getMessage());
			} catch (IllegalArgumentException e) {
				log.error("JWT claims string is empty: {}", e.getMessage());
			}
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}
		
		// token 검증이 되고 인증 정보가 존재하지 않는 경우 spring security 인증 정보 저장
		if(adminId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			
			AdminDTO adminDTO = new AdminDTO();
			
			try {
				adminDTO = adminService.loadAdminByAdminId(adminId);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(jwtTokenProvider.validateToken(jwtToken, adminDTO)) {
				UsernamePasswordAuthenticationToken authenticationToken =
						new UsernamePasswordAuthenticationToken(adminDTO, null ,adminDTO.getAuthorities());
				
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		}
		
		// accessToken 인증이 되었다면 refreshToken 재발급이 필요한 경우 재발급
		try {
			if(adminId != null) {
				jwtTokenProvider.reGenerateRefreshToken(adminId);
			}
		}catch (Exception e) {
			log.error("[JwtRequestFilter] refreshToken 재발급 체크 중 문제 발생 : {}", e.getMessage());
		}
		
		filterChain.doFilter(request,response);
		
	}
}
