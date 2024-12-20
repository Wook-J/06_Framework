package edu.kh.project.board.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.kh.project.board.model.dto.Comment;
import edu.kh.project.board.model.service.CommentService;
import lombok.RequiredArgsConstructor;

/* @RestController (REST API 구축을 위해 사용하는 컨트롤러)
 * => @Controller (요청/응답 제어하는 역할임을 명시 + Bean 으로 등록)
 * 	  + @ResponseBody (응답 본문으로 응답데이터 자체를 반환)
 * 
 * -> 모든 요청에 대한 응답을 응답 본문으로 반환하는 컨트롤러임!!!
 * 
 * cf) 전부 다 비동기 요청을 받으려는 Controller 인 경우에 사용!!
 * */
@RestController
@RequestMapping("comment")
@RequiredArgsConstructor
public class CommentController {
	
	// fetch 로 비동기 요청만 받을 예정
	// "/comment" 요청이 오면 해당 컨트롤러에서 잡아서 처리함 (@RequestMapping)
	// @ResponseBody 를 매번 메서드에 추가해야함....!!
	// -> @Controller 대신에 @RestController 를 작성!!!
	
	private final CommentService service;
	
	/* *************** 메서드 *************** */

	/** 댓글 목록 조회
	 * @param boardNo
	 * @return
	 */
	@GetMapping("")		// comment 로 시작되는 GetMapping
	public List<Comment> select(@RequestParam("boardNo") int boardNo){
		
		// List -> JSON(문자열)로 변환해서 응답 -> JS
		// HttpMessageConverter 가 변환 해 줌!!
		
		return service.select(boardNo);
	}
	
	/** 댓글/답글 등록
	 * @param comment
	 * @return
	 */
	@PostMapping("")
	public int insert(@RequestBody Comment comment) {
		return service.insert(comment);
	}
	
	/** 댓글/답글 삭제
	 * @param commentNo
	 * @return
	 */
	@DeleteMapping("")
	public int delete(@RequestBody int commentNo) {
		return service.delete(commentNo);
	}
	
	/** 댓글/답글 수정
	 * @param comment
	 * @return
	 */
	@PutMapping("")
	public int update(@RequestBody Comment comment) {
		return service.update(comment);
	}
	
}
