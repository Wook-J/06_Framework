package edu.kh.project.common.interceptor;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import edu.kh.project.board.model.service.BoardService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/* Interceptor : 요청/응답/뷰 완성 후 가로채는 객체 (Spring spec)
 * 
 * * HandlerInterceptor 인터페이스를 상속받아서 구현해야함!
 * 
 * - preHandle (전처리) : Dispatcher Servlet -> Controller 과정에서 수행
 * 
 * - postHandle (후처리) : Controller -> Dispatcher Servlet 과정에서 수행
 * 
 * - afterCompletion (뷰 완성(forward 코드 해석) 후) : View Resolver -> Dispatcher Servlet 과정에서 수행  
 * 
 * */
public class BoardTypeInterceptor implements HandlerInterceptor{
	
	@Autowired
	private BoardService service;

	@Override	// 전처리 (요청이 들어오기 전 실행)
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		// boardTypeList 를 DB에서 얻어오기
		
		// page < request < session(client 마다 1개) < application
		// application scope : 서버 종료시 까지 유지되는 Servlet 내장 객체
		// - 서버 내에 딱 1개만 존재!!!(모든 client 가 공용으로 사용)
		
		// application scope 객체 얻어오기
		ServletContext application = request.getServletContext();
		
		// application scope 에 "boardTypeList"가 없을 경우
		if(application.getAttribute("boardTypeList") == null) {
			
			// boardTypeList 를 조회하는 서비스 호출
			List<Map<String, Object>> boardTypeList = service.selectBoardTypeList();
			
			// 조회 결과를 application scope 에 추가
			application.setAttribute("boardTypeList", boardTypeList);
		}
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
	
				// 응답을 가지고 Dispatcher Servlet 에게 돌려보내기 전
	@Override	// 후처리(요청이 처리된 후 뷰가 랜더링되기 전에 실행)
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
	@Override	// 뷰 완성 후 (뷰 랜더링이 끝난 후 실행) 
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
	
}
