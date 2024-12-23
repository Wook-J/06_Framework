package edu.kh.project.common.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/* Filter : 요청, 응답 시 걸러내거나 추가할 수 있는 객체
 * 
 * [필터 클래스 생성 방법]
 * 1. jakarta.servlet.Filter 인터페이스 상속 받기
 * 2. doFilter() 메서드 오버라이딩
 * 
 * */

// 로그인이 되어 있지 않는 경우 특정 페이지로 돌아가게 함
public class LoginFilter implements Filter{
	
	// 필터 동작을 정의하는 메서드
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		// ServletRequest : HttpServletRequest 의 부모 타입
		// ServletResponse : HttpServletResponse 의 부모 타입
		
		// Session 필요함 -> loginMember 가 session 에 담김
		
		// HTTP 통신이 가능한 형태(자식형태)로 다운캐스팅
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		//-------- 게시판에 프로필사진 안 뜨는 문제해결 위해 추가작성 시작 ------
		// 현재 요청의 URI를 가져옴
		String path = req.getRequestURI();
		
		// 요청 URI가 "/myPage/profile/" 로 시작하는지 확인
		if(path.startsWith("/myPage/profile/")) {
			chain.doFilter(request, response);		// 필터를 통과하도록 함
			return;									// 필터 통과 후 아래코드 수행하지 않도록 함
		}
		
		//-------- 추가작성 여기까지(241115 오후 2시10분) -----------------------
		
		// Session 얻어오기
		HttpSession session = req.getSession();
		
		// 세션에서 로그인한 회원 정보를 얻어옴
		
		// 얻어왔으나 회원정보 없을 때(로그인이 되어 있지 않은 상태)
		// /loginError 재요청 : resp 를 이용해서 원하는 곳으로 redirect
		if(session.getAttribute("loginMember") == null) resp.sendRedirect("/loginError");
		else {		// 로그인이 되어 있는 경우
			// FilterChain
			// - 다음 필터 또는 Dispatcher Servlet 과 연결된 객체로 넘어감
			// 다음 필터로 요청/응답 객체 전달
			// 만약 다음 필터가 없으면 Dispatcher Servlet으로 request, response 전달
			chain.doFilter(request, response);
		}
	}
	
}
