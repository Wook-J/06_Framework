package edu.kh.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller		// 요청/응답을 제어하는 역할 명시 + Bean(== 스프링이 만들고 관리하는 객체) 등록
public class ExampleController {
	/*
	 * 1) @RequsetMapping("주소")
	 * 
	 * 2) @GetMapping("주소")		: Get (조회) 방식 요청 매핑
	 *    @PostMapping("주소")		: Post (삽입) 방식 요청 매핑
	 *    @PutMapping("주소")		: Put (수정) 방식 요청 매핑 (form, a태그 요청 불가)
	 *    @DeleteMapping("주소")	: Delete (삭제) 방식 요청 매핑 (form, a태그 요청 불가)
	 *    -> 위 4개를 Rest API 라고 부름...(아직은 Get, Post 만 쓸 예정)
	 * */
	
	/* Spring Boot 에서는 요청주소 앞에 "/"가 없어도 요청처리가 잘되지만
	 * 보통 "/"를 작성 안하는 것을 권장
	 * 
	 * -> 프로젝트를 사용자가 사용할 수 있게끔 인터넷상에 배포(최종목표)
	 * -> 호스팅 서비스(AWS -> 프리티어 인스턴스 -> 리눅스 체제)
	 * -> 리눅스에서 요청주소 앞에 "/"가 있으면 build error 발생
	 * 
	 * @RequestMapping("/") 인 경우(메인페이지 요청)엔 "/"만 있어도 리눅스에서 에러발생X
	 * 
	 * */
	
	@GetMapping("example")		// /example GET 방식 요청 매핑함!
	public String exampleMethod() {
		
		// forward 하려는 html 파일 경로를 return 작성
		// 단, ViewResolver 가 제공하는 Thymeleaf의 접두사, 접미사는 제외하고 작성
		return "example";		// classpath:/templates/example.html
	}

}
