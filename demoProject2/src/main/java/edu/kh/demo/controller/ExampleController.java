package edu.kh.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.kh.demo.model.dto.Student;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Controller		// 요청/응답 제어 역할 명시 + Bean 등록
@RequestMapping("example")		// /example 로 시작하는 주소를 해당 컨트롤러에 매핑
@Slf4j			// lombok 라이브러리가 제공하는 로그 객체 자동생성 어노테이션
public class ExampleController {

	/* Model
	 * - Spring 에서 데이터 전달 역할을 하는 객체
	 * - org.springframework.ui 패키지에 존재
	 * 
	 * - 기본 scope : request
	 * - @SessiontAttribute 와 함께 사용 시 session scope 변환
	 * 
	 * [기본 사용법]
	 * Model.addAttribute("key", value);
	 * 
	 * */
	@GetMapping("ex1")	// /example/ex1 GET 방식 요청을 매핑
	public String ex1(HttpServletRequest req, Model model) {
		
		// Servlet/JSP 내장객체 범위(scope)
		// page < request < session < application
		
		// request scope 에 값 세팅
		req.setAttribute("test1", "HttpServletRequest 로 전달한 값");
		model.addAttribute("test2", "Model 로 전달한 값");
		
		// 단일 값(숫자, 문자열)을 Model 을 이용해서 html 로 전달
		model.addAttribute("productName", "종이컵");
		model.addAttribute("price", 2000);
		
		// 복수 값(배열, List)을 Model 을 이용해서 html 로 전달
		List<String> fruitList = new ArrayList<>();
		fruitList.add("사과");
		fruitList.add("딸기");
		fruitList.add("바나나");
		
		model.addAttribute("fruitList", fruitList);
		
		// DTO 객체를 Model 을 이용해서 html 로 전달
		Student std = new Student();
		std.setStudentNo("12345");
		std.setName("홍길동");
		std.setAge(22);
		model.addAttribute("std", std);
		
		// List<Student> 객체를 Model 을 이용해서 html 로 전달
		// new ArrayList<>() 의 제네릭 부분에 Student 안써도 문법상 오류는 없음
		List<Student> stdList = new ArrayList<>();
		stdList.add(new Student("11111","김일번", 20));
		stdList.add(new Student("22222","최이번", 23));
		stdList.add(new Student("33333","홍삼번", 22));
		model.addAttribute("stdList", stdList);
		
		// src/main/resources/templates/example/ex1.html 으로 요청 위임
		return "example/ex1";
	}
	
	@PostMapping("ex2")	// /example/ex2 POST 방식 요청을 매핑
	public String ex2(Model model) {
		
		// Model : Spring 에서 데이터 전달 역할을 하는 객체
		// -> 기본적으로 request scope 이지만 session 으로 확장 가능
		
		model.addAttribute("str", "<h1>테스트 중 &times; </h1>");
		
		// 접두사 prefix : classpath:/templates/
		// 접미사 suffix : .html
		// src/main/resources/templates/example/ex2.html 으로 요청 위임(forward)
		return "example/ex2";
	}
	
	@GetMapping("ex3")	// /example/ex3 GET 방식 요청을 매핑
	public String ex3(Model model) {
		
		model.addAttribute("key", "제목");
		model.addAttribute("query", "검색어");
		
		model.addAttribute("boardNo", 10);
		
		// 접두사 prefix : classpath:/templates/
		// 접미사 suffix : .html
		// src/main/resources/templates/example/ex3.html 으로 요청 위임(forward)
		return "example/ex3";
	}
	
	@GetMapping("ex3/{number}")
	public String pathVariableTest( @PathVariable("number") int number) {
		// 주소 중 {number} 부분의 값을 가져와서 매개변수로 저장하고
		// controller 에서 사용할 수 있도록 해줌
		// + request Scope 에 자동 세팅(★)
		// -> 이건 좀 빡세게 익혀야 할 듯...! 하나의 메서드로 조회를 한번에 해결함...!
		
		log.debug("number : " + number);
		
		// src/main/resources/templates/example/testResult.html 으로 요청 위임(forward)
		return "example/testResult";
	}
	
	@GetMapping("ex4")	// /example/ex4 GET 방식 요청을 매핑
	public String ex4(Model model) {
		
		Student std = new Student("67890", "잠만보", 22);
		model.addAttribute("std", std);
		
		model.addAttribute("num", 200);

		// src/main/resources/templates/example/ex4.html 으로 요청 위임(forward)
		return "example/ex4";
	}
	
	@GetMapping("ex5")	// /example/ex4 GET 방식 요청을 매핑
	public String ex5(Model model) {
		
		model.addAttribute("message", "타임리프 + Javascript 사용 연습");
		model.addAttribute("num1", 12345);
		
		Student std = new Student();
		std.setStudentNo("22222");
		model.addAttribute("std", std);
		
		// src/main/resources/templates/example/ex5.html 으로 요청 위임(forward)
		return "example/ex5";
	}
	
}
