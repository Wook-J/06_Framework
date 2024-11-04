package edu.kh.todo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.kh.todo.model.dto.Todo;
import edu.kh.todo.model.service.TodoService;
import lombok.extern.slf4j.Slf4j;

@Slf4j			// 로그 객체 자동생성(lombok)
@Controller		// 요청/응답 제어 역할 명시 + Bean 등록
public class MainController {
	
	@Autowired	// 등록된 Bean 중 같은 타입이거나 상속관계 DI(의존성주입)
	private TodoService service;
	
	@RequestMapping("/")
	public String mainPage(Model model) {
		
//		log.debug("service : " + service);
		// service : edu.kh.todo.model.service.TodoServiceImpl@7355f5b8
		
		//----------------------- test 용 -------------------------
		// todoNo가 1인 todo를 제목 조회하여 request scope에 추가
		String testTitle = service.testTitle();
		model.addAttribute("testTitle", testTitle);
		//-----------------------------------------------------------
		
		// TB_TODO 테이블에 저장된 전체 할 일 목록 조회하기 + 완료된 할 일 개수
		// 알맞은 Service 메서드 호출 후 결과 반환 받기
		Map<String, Object> map = service.selectAll();
		
		// map에 담긴 내용 추출
		List<Todo> todoList = (List<Todo>) map.get("todoList");
		int completeCount = (int) map.get("completeCount");
		
		// Model을 이용해서 조회결과를  request scope에 추가
		model.addAttribute("todoList", todoList);
		model.addAttribute("completeCount", completeCount);
		
		// 접두사(prefix) : classpath:/templates/
		// 접미사(suffix) : .html
		return "common/main";		// forward
	}
}
