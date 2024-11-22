package edu.kh.project.board.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.service.BoardService;
import edu.kh.project.board.model.service.EditBoardService;
import edu.kh.project.member.model.dto.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("editBoard")
@Slf4j
@RequiredArgsConstructor
public class EditBoardController {
	
	private final EditBoardService service;
	private final BoardService boardService;
	
	/* *************** 메서드 *************** */
	
	/** 게시글 작성 화면 전환(GET)
	 * @return
	 */
	@GetMapping("{boardCode:[0-9]+}/insert")
	public String boardInsert(@PathVariable("boardCode") int boardCode) {
		// requestScope에 boardCode 가 실려있음!
		
		return "board/boardWrite";	// templates/board/boardWrite.html 로 forward
	}
	
	/** 게시글 작성(POST)
	 * @param loginMember : 로그인한 회원번호를 얻어오는 용도 (세션에 등록)
	 * @param boardCode : 어떤 게시판에 작성할 글인지 구분 (1, 2, ...)
	 * @param inputBoard : boardTitle, boardContent
	 * @param images : 제출된 file 타입 input 태그가 전달한 데이터들 (이미지 파일...)
	 * @return
	 */
	@PostMapping("{boardCode:[0-9]+}/insert")
	public String boardInsert(@SessionAttribute("loginMember") Member loginMember,
							@PathVariable("boardCode") int boardCode,
							Board inputBoard,
							@RequestParam("images") List<MultipartFile> images,
							RedirectAttributes ra) throws Exception {
		
		/* List<MultipartFile> images
		 * - 5개 모두 업로드 O : 0 ~ 4 인덱스에 파일 저장됨
		 * - 5개 모두 업로드 X : 0 ~ 4 인덱스에 파일 저장 X
		 * - 2번 인덱스만 업로드 : 2번 인덱스만 파일 저장 (나머지 인덱스는 저장 X)
		 * 
		 * [문제점]
		 * - 파일이 선택되지 않은 input 태그도 제출되고 있음
		 *   (제출은 되어 있는데 데이터는 없음)
		 * 
		 * -> 파일 선택이 안된 input 태그 값을 서버에 저장하려고 하면 오류 발생함!
		 * 
		 * [해결방법]
		 * - 무작정 서버에 저장하는 것이 아닌 제출된 파일이 있는지 확인하는 로직을 추가구성!
		 * 
		 * -> List 의 각 인덱스에 들어있는 MultipartFile에 실제로 제출한 파일이 있는 지 확인
		 *    (ServiceImpl 에서 처리할 예정)
		 * 
		 * (+ List 요소의 인덱스 번호 == IMG_ORDER)
		 * */
		
		// 1. boardCode, 로그인한 회원 번호를 inputBoard에 세팅
		inputBoard.setBoardCode(boardCode);
		inputBoard.setMemberNo(loginMember.getMemberNo());
		// -> inputBoard에 boardTitle, boardContent, boardCode, memberNo 세팅
		
		// 2. 서비스 메서드 호출 후 결과 반환
		// -> 성공 시 [상세 조회]를 요청할 수 있도록 삽입된 게시글 번호를 반환받기
		int boardNo = service.boardInsert(inputBoard, images);
		
		// 3. 서비스 결과에 따라 message, 리다이렉트 경로 지정
		String path = null;
		String message = null;
		
		if(boardNo > 0) {
			path = "/board/" + boardCode + "/" + boardNo;		// /board/1/2002 -> 상세 조회
			message = "게시글이 작성되었습니다!";
			
		} else {
			path = "insert";		// 상대경로 -> /editBoard/1/insert (redirect 시 GET 요청)
			message = "게시글 작성 실패";
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:" + path;
	}
	
	/** 게시글 수정 화면 전환
	 * @param loginMember : 로그인한 회원이 작성한 글인지 검사하는 용도
	 * @param boardCode : 게시판 종류
	 * @param boardNo : 게시글 번호
	 * @param model : forward 시 request scope 로 값을 전달하는 용도
	 * @param ra : redirect 시 request scope 로 값을 전달하는 용도
	 * @return
	 */
	@GetMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}/update")
	public String boardUpdate(@SessionAttribute("loginMember") Member loginMember,
							@PathVariable("boardCode") int boardCode,
							@PathVariable("boardNo") int boardNo,
							Model model,
							RedirectAttributes ra) {
		
		// 수정화면에 출력할 기존의 제목/내용/이미지 조회
		// -> 게시글 상세 조회
		Map<String, Integer> map = new HashMap<>();
		map.put("boardCode", boardCode);
		map.put("boardNo", boardNo);
		
		// boardServiceImpl에 있는 메서드 활용
		Board board = boardService.selectOne(map);
		
		String message = null;
		String path = null;
		
		if(board == null) {
			message = "해당 게시글이 존재하지 않습니다.";
			path = "redirect:/";
			ra.addFlashAttribute("message", message);
			
		} else if (board.getMemberNo() != loginMember.getMemberNo()) {
			message = "자신이 작성한 글만 수정할 수 있습니다";
			path = String.format("redirect:/board/%d/%d", boardCode, boardNo);
			ra.addFlashAttribute("message", message);
			
		} else {
			path = "board/boardUpdate";		// classpath:/templates/board/boardUpdate.html
			model.addAttribute("board", board);
		}
		
		return path;
	}
	
	/** 게시글 수정
	 * @param loginMember : 로그인한 회원의 번호 이용 예정
	 * @param boardCode : 게시판 종류
	 * @param boardNo : 수정할 게시글 번호
	 * @param inputBoard : 커맨드 객체 (boardTitle, boardContent)
	 * @param images : 제출된 input type="file"의 모든 요소 (5개가 다 넘어옴)
	 * @param deleteOrderList : 삭제된 이미지 순서가 기록된 문자열 (imgOrder와 비교옹)
	 * @param cp : 수정 성공 시 이전 파라미터 유지용
	 * @param ra : redirect 시 request scope 로 값 전달용
	 * @return
	 */
	@PostMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}/update")
	public String boardUpdate(@SessionAttribute("loginMember") Member loginMember,
						@PathVariable("boardCode") int boardCode,
						@PathVariable("boardNo") int boardNo,
						@ModelAttribute Board inputBoard,
						@RequestParam("images") List<MultipartFile> images,
						@RequestParam(value="deleteOrderList", required=false) String deleteOrderList,
						@RequestParam(value="cp", required=false, defaultValue="1") int cp,
						RedirectAttributes ra) throws Exception {
		
		// 1. 커맨드 객체 (inputBoard)에 boardCode, boardNo, memberNo 세팅
		inputBoard.setBoardCode(boardCode);
		inputBoard.setBoardNo(boardNo);
		inputBoard.setMemberNo(loginMember.getMemberNo());
		// inputBoard 에 boardNo, boardTitle, boardContent, boardCode, memberNo 세팅됨!
		
		// 2. 게시글 수정 서비스 호출 후 결과 반환
		int result = service.boardUpdate(inputBoard, images, deleteOrderList);
		
		// 3. 서비스 결과에 따라 응답 제어
		String message = null;
		String path = null;
		
		if(result > 0) {
			message = "게시글이 수정되었습니다";
			path = String.format("/board/%d/%d?cp=%d", boardCode, boardNo, cp);
			
		} else {
			message = "수정 실패";
			path = "update";		// GET (수정화면 전환) 리다이렉트 하는 상대경로
		}
		
		ra.addFlashAttribute("message", message);
		return "redirect:" + path;
	}
	
	// /editBoard/1/2000/delete?cp=1
	/** 게시글 삭제
	 * @param loginMember : 현재 로그인한 회원 번호 사용 예정
	 * @param boardCode : 게시판 종류 번호
	 * @param boardNo : 게시글 번호
	 * @param cp : 삭제 시 게시글 목록으로 리다이렉트 할 때 사용할 페이지 번호
	 * @param ra : redirect 시 request scope 로 값 전달용
	 * @return
	 */
	@RequestMapping(value="{boardCode:[0-9]+}/{boardNo:[0-9]+}/delete", method={RequestMethod.GET, RequestMethod.POST})
	public String boardDelete(@SessionAttribute("loginMember") Member loginMember,
							@PathVariable("boardCode") int boardCode,
							@PathVariable("boardNo") int boardNo,
							@RequestParam(value="cp", required=false, defaultValue="1") int cp,
							RedirectAttributes ra) {
		
		Map<String, Integer> map = new HashMap<>();
		map.put("boardCode", boardCode);
		map.put("boardNo", boardNo);
		map.put("memberNo", loginMember.getMemberNo());
		
		int result = service.boardDelete(map);
		
		String path = null;
		String message = null;
		
		if(result > 0) {
			path = String.format("/board/%d?cp=%d", boardCode, cp);	// /board/1?cp=7
			message = "게시글이 삭제되었습니다";
			
		} else {
			path = String.format("/board/%d/%d?cp=%d", boardCode, boardNo, cp);
			message = "게시글 삭제 실패";
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:" + path;
	}
}









