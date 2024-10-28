package edu.kh.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


// Java 객체 : new 연산자에 의해 Heap 영역에 클래스에 작성된 내용대로 생성된 것
// instance : 개발자가 직접 만들고 관리하는 객체

// Bean : Spring Container 가 만들고 관리하는 객체

@Controller		// 요청/응답을 제어할 컨트롤러 역할임을 명시 + Bean 등록
public class TestController {

	// 기존 Servlet : 클래스 단위로 하나의 요청(주소)만 처리 가능
	// Spring 에서 Controller : 메서드 단위로 요청 처리 가능
	
	// @RequestMapping("요청주소") : 요청주소를 처리할 메서드를 매핑하는 어노테이션
	
	// 1) 메서드에 작성 : 요청주소와 해당 메서드를 매핑
	// - GET/POST 가리지 않고 매핑 (속성을 통해서 지정 가능 or 다른 어노테이션을 이용해서도 가능)
	// - ex) : @RequestMapping(value="/hello", method=RequestMethod.POST)
	// import org.springframework.web.bind.annotation.RequestMethod; 필요!

	/*
	@RequestMapping(value="/hello", method=RequestMethod.POST)
	public String hello() {
		// 메서드 로직...
		return "경로...";
	}
	*/
	
	// 2) 클래스에 작성 : (보통) 공통 주소를 매핑할 경우
	// - ex) /todo/insert, /todo/select, /todo/update ... 
	// -> @RequestMapping("/todo")를 @Controller 위에 작성하고 개별 메서드에 뒷부분 입력
	
	/* 
	@RequestMapping("/insert")		-> /todo/insert
	public String insert() {
		메서드 로직...
		return "경로";
	}
	
	@RequestMapping("/select")		-> /todo/select
	public String insert() {
		메서드 로직...
		return "경로";
	}
	
	@RequestMapping("/update")		-> /todo/update
	public String insert() {
		메서드 로직...
		return "경로";
	}
	*/
	
	@RequestMapping("/test")		// /test 요청 시 처리할 메서드로 매핑하는 어노테이션
	public String testMethod() {
		System.out.println("/test 요청 받음");
		
		/* Controller 메서드의 반환형이 String 인 이유
		 * - 메서드에서 반환되는 문자열이 forward 할 html 파일의 경로가 되기 때문
		 * 
		 * Thymeleaf : JSP 대신 사용하는 템플릿 엔진 (html 형태)
		 * 
		 * classpath: == src/main/resources
		 * 접두사(prefix) : classpath:/templates/
		 * 접미사(suffix) : .html
		 * */
		return "test";		// -> src/main/resources/templates/test.html 로 forward
		
		/* 접두사, 접미사, forward 설정은 View Resolver 객체가 담당 */
	}
	
}
