package edu.kh.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.todo.model.dto.Todo;
import edu.kh.todo.model.service.TodoService;

@Controller		// 요청/응답 제어 역할 명시 + Bean 등록
@RequestMapping("todo")		// "/todo"로 시작하는 모든 요청 매핑
public class TodoController {
	
	@Autowired	// 같은 타입(OR 상속관계) Bean 을 의존성 주입(DI)
	private TodoService service;
	
	@PostMapping("add")		// "/todo/add" POST 방식 요청 매핑
	public String addTodo(@RequestParam("todoTitle") String todoTitle,
						@RequestParam("todoContent") String todoContent,
						RedirectAttributes ra) {
		
		// RedirectAttributes : 리다이렉트 시 값을 1회성으로 전달하는 객체
		
		// RedirectAttributes.addFlashAttribute("key", value) 형식으로 세팅
		// request scope -> session scope 로 잠시 변환
		// 응답 전 : request scope
		// redirect 중 : session scope 로 이동
		// 응답 후 : request scope 로 복귀
		
		// 서비스 메서드 호출 후 결과 반환 받기
		int result = service.addTodo(todoTitle, todoContent);
		
		// 삽입 결과에 따라 message 값 지정
		String message = null;
		
		if(result > 0) message = "할 일 추가 성공";
		else message = "할 일 추가 실패...";
		
		// 리다이렉트 후 1회성으로 사용할 데이터를 속성으로 추가
		ra.addFlashAttribute("message", message);
		
		return "redirect:/";	// 메인페이지 재요청
	}
	
	@GetMapping("detail")	// "/todo/detail" GET 방식 요청 매핑
	public String detailTodo(@RequestParam("todoNo") int todoNo, 
							Model model, RedirectAttributes ra) {
		// RedirectAttributes : 리다이렉트 시 데이터를 session scope 로
		// 잠시 이동시킬 수 있는 1회성 값 전달용 객체
		
		Todo todo = service.todoDetail(todoNo);
		
		String path = null;
		
		if(todo != null) {						// 조회결과 있을 경우
			path = "todo/detail";				// todo/detail.html 로 forward
			model.addAttribute("todo", todo);	// request scope에 값 세팅
		}
		else {									// 조회결과 없을 경우
			path = "redirect:/";				// 메인페이지로 redirect
			ra.addFlashAttribute("message", "해당 할 일이 존재하지 않습니다");
		}
		
		return path;
		// classpath: == src/main/resources
		// 접두사 : classpath:/templates/
		// 접미사 : .html		
	}

	/**
	 * @param todo : 커맨드 객체(@ModelAttribute 생략가능)
	 * - 파라미터의 key와 Todo 객체의 필드명이 일치하면
	 *   일치한 필드값이 세팅된 상태!
	 *   
	 * - 여기선 todoNo, complete 두 필드가 세팅됨!
	 *   (todo/changeComplete?todoNo=5&complete=Y)
	 * @return
	 */
	@GetMapping("changeComplete")
	public String changeComplete(@ModelAttribute Todo todo, RedirectAttributes ra) {
		
		int result = service.changeComplete(todo);
		
		String message = null;
		if(result > 0) message = "변경 성공!!!";
		else message = "변경 실패...";
		
		ra.addFlashAttribute("message", message);
		
		// 상대 경로 (현재 위치) (마지막 주소부분이 바뀜)
		// 현재 주소 : /todo/changeComplete
		// 재요청 주소 : /todo/detail?todoNo=~~
		return "redirect:detail?todoNo=" + todo.getTodoNo();
	}
	
	@GetMapping("update")	// "/todo/update" GET 방식 요청 매핑
	public String update(@RequestParam("todoNo") int todoNo, Model model) {
		
		Todo todo = service.todoDetail(todoNo);
		model.addAttribute("todo", todo);
		
		return "todo/update";
	}
	
	@PostMapping("update")	// "/todo/update" POST 방식 요청 매핑
	public String update2(Todo todo, Model model, RedirectAttributes ra) {
		
		int result = service.updateTodo(todo);
		
		String message = null;
		if(result > 0) message = "수정 성공!!!";
		else message = "수정 실패...";
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:detail?todoNo=" + todo.getTodoNo();
	}
	
	@GetMapping("delete")	// "/todo/delete" GET 방식 요청 매핑
	public String delete(@RequestParam("todoNo") int todoNo,
						Model model, RedirectAttributes ra) {
		
		int result = service.deleteTodo(todoNo);
		
		String message = null;
		if(result > 0) message = "삭제 성공!!!";
		else message = "삭제 실패...";
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:/";
	}
}









