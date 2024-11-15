package edu.kh.project.board.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.dto.BoardImg;
import edu.kh.project.board.model.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("board")
@Slf4j
@RequiredArgsConstructor
public class BoardController {

	private final BoardService service;
	
	/** 게시글 목록 조회
	 * @param boardCode : 게시판 종류 구분 번호 (1/ 2/ 3 ...)
	 * @param cp : 현재 조회 요청한 페이지 번호 (없으면 1)
	 * @return
	 * 
	 * "{boardCode}" 만 작성한 경우
	 * - 사용자가 /board 이하 1레벨 자리에 어떤 주소값이 들어오든 모두 이 메서드에 GET 매핑
	 * - /board 이하 1레벨자리에 숫자로된 요청 주소가 작성되어 있을 때만 동작 -> 정규 표현식 이용
	 * 
	 * [0-9] : 한 칸에 0~9 사이 숫자 입력 가능
	 * + : 하나 이상
	 * [0-9]+ : 모든 숫자를 나타낼 수 있음
	 * 
	 */
	@GetMapping("{boardCode:[0-9]+}")
	public String selectBoardList(@PathVariable("boardCode") int boardCode,
							@RequestParam(value="cp", required=false, defaultValue="1") int cp,
							Model model) {
		
		// 조회 서비스 호출 후 결과 반환
		Map<String, Object> map = null;
		map = service.selectBoardList(boardCode, cp);
		
		// model 에 반환받은 값을 등록
		model.addAttribute("pagination", map.get("pagination"));
		model.addAttribute("boardList", map.get("boardList"));
				
		// forward : classpath:/templates/board/boardList.html
		return "board/boardList";
	}
	
	// 상세 조회 요청 주소
	// /board/1/2001?cp=2
	@GetMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}")
	public String boardDetail(@PathVariable("boardCode") int boardCode,
							@PathVariable("boardNo") int boardNo,
							Model model,
							RedirectAttributes ra) {
		
		// 게시글 상세조회 서비스 호출
		
		// 1) Map 으로 전달할 파라미터 묶기
		Map<String, Integer> map = new HashMap<>();
		map.put("boardCode", boardCode);
		map.put("boardNo", boardNo);
		
		// 2) 서비스 호출
		Board board = service.selectOne(map);
		
//		log.debug("조회된 board : " + board);	// 확인용
		
		String path = null;
		
		if(board == null) {		// 조회 결과가 없는 경우
			path = "redirect:/board/" + boardCode;
			ra.addFlashAttribute("message", "게시글이 존재하지 않습니다.");
			
		} else {				// 조회 결과가 있는 경우
			path = "board/boardDetail";
			model.addAttribute("board", board);	// 게시글 일반 내용 + imageList + commentList
			
			if(!board.getImageList().isEmpty()) {
				BoardImg thumbnail = null;
				
				// imageList의 0번 인덱스 == 가장 빠른 순서(ORDER BY IMG_ORDER)
				if(board.getImageList().get(0).getImgOrder() == 0) {
					thumbnail = board.getImageList().get(0);
				}
				
				model.addAttribute("thumbnail", thumbnail);
				model.addAttribute("start", thumbnail != null ? 1 : 0);
			}
		}
		
		return path;
	}
}
