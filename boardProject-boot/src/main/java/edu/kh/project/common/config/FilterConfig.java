package edu.kh.project.common.config;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.kh.project.common.filter.LoginFilter;

// 만들어 놓은 Filter 클래스가 언제 적용될 지 설정
@Configuration		// 서버가 켜질 때 해당 클래스 내 모든 메서드가 실행됨
public class FilterConfig {

	@Bean	// 반환된 객체(LoginFilter 로 타입을 제한)를 Bean 으로 등록
	public FilterRegistrationBean<LoginFilter> loginFilter(){
		// FilterRegistrationBean : 필터를 Bean 으로 등록하는 객체
		
		FilterRegistrationBean<LoginFilter> filter = new FilterRegistrationBean<>();
		
		filter.setFilter(new LoginFilter());			// 사용할 필터 객체 추가
		
		// 필터가 동작할 URL을 세팅  (/myPage/* : myPage 로 시작하는 모든 요청)
		String[] filteringURL = {"/myPage/*", "/editBoard/*", "/chatting/*"};
		filter.setUrlPatterns(Arrays.asList(filteringURL));	// Collection이 () 안에 있어야함
		
		filter.setName("loginFilter");					// 필터 이름 지정
		filter.setOrder(1);								// 필터 순서 지정
		
		return filter;	// 반환된 객체가 필터를 생성해서 Bean 으로 등록
	}
}
