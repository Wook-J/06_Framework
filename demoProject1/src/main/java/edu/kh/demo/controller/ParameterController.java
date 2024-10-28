package edu.kh.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Controller		// 요청/응답을 제어하는 역할 명시 + Bean(Spring 이 만들고 관리하는 객체) 등록
@RequestMapping("param")	// /param 으로 시작하는 요청을 현재 컨트롤러로 매핑
@Slf4j			// (★) log 를 이용한 메세지 출력 시 사용하는 어노테이션 (클래스단에 작성!) (Lombok)
public class ParameterController {
	
	@GetMapping("main")		// /param/main Get 방식 요청 매핑
	public String paramMain() {
		
		// 접두사 : clsspath:/templates
		// 접미사 : .html
		// -> src/main/resources/templates/param/param-main.html 로 forward
		return "param/param-main";
	}
	
	/* HttpServletRequest : 요청한 클라이언트의 정보와 함께 제출한 파라미터 등을 저장한 객체
	 * - 클라이언트가 요청 보낼 시 생성되는 객체
	 * 
	 * Spring 의 Controller 메서드 작성 시 매개변수에 원하는 객체를 작성하면
	 * 존재하고 있는 객체를 바인딩 또는 없으면 생성해서 바인딩
	 * 
	 * --> ArgumentResolver (전달 인자 해결사) 
	 * */
	@PostMapping("test1")
	public String paramTest1(HttpServletRequest req) {
		
		String inputName = req.getParameter("inputName");
		int inputAge = Integer.parseInt(req.getParameter("inputAge"));
		String inputAddress = req.getParameter("inputAddress");
		
		// debug : 보통 코드 오류 해결
		// -> 코드 오류는 없는데 정상 수행이 안될 때
		// -> 값이 잘못된 경우 -> 값 추적용
		log.debug("inputName : " + inputName);		// (★) DEBUG 레벨로 찍힘!
		log.debug("inputAge : " + inputAge);
		log.debug("inputAddress : " + inputAddress);
		
		/* Spring 에서 Redirect(재요청) 하는 방법!
		 * - Controller 메서드 반환 값에 "redirect:요청주소"; 작성
		 * */
		return "redirect:/param/main";
	}
}
