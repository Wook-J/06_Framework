package edu.kh.project.board.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.dto.BoardImg;
import edu.kh.project.board.model.service.BoardService;
import edu.kh.project.member.model.dto.Member;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
	 * @param paramMap(검색할 경우) : 제출된 파라미터가 모두 저장된 Map (key, query)
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
							@RequestParam Map<String, Object> paramMap,
							Model model) {
		
		// 조회 서비스 호출 후 결과 반환
		Map<String, Object> map = null;
		
		// 검색이 아닌 경우 --> paramMap == {}
		// 검색인 경우		--> paramMap == {key=t, query=짱구}
		if(paramMap.get("key") == null) map = service.selectBoardList(boardCode, cp);
		else {
			paramMap.put("boardCode", boardCode);
			// --> paramMap == {key=t, query=짱구, boardCode=1}
			
			map = service.searchList(paramMap, cp);
		}

		
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
						@SessionAttribute(value="loginMember", required = false) Member loginMember,
						Model model,
						HttpServletRequest req,		// 요청에 담긴 쿠키 얻어오기
						HttpServletResponse resp,	// 새로운 쿠키를 만들어서 응답하기
						RedirectAttributes ra) {
		
		// 게시글 상세조회 서비스 호출
		
		// 1) Map 으로 전달할 파라미터 묶기
		Map<String, Integer> map = new HashMap<>();
		map.put("boardCode", boardCode);
		map.put("boardNo", boardNo);
		
		// 로그인 상태인 경우에만 memberNo 추가
		if(loginMember != null) map.put("memberNo", loginMember.getMemberNo());
		
		// 2) 서비스 호출
		Board board = service.selectOne(map);
		
//		log.debug("조회된 board : " + board);	// 확인용
		
		String path = null;
		
		if(board == null) {		// 조회 결과가 없는 경우
			path = "redirect:/board/" + boardCode;
			ra.addFlashAttribute("message", "게시글이 존재하지 않습니다.");
			
		} else {				// 조회 결과가 있는 경우
			
			/* ---------- 쿠키를 이용한 조회수 증가 ---------- */
			// 비회원 또는 로그인한 회원의 글이 아닌 경우 (== 글쓴이를 뺀 다른 사람)
			if(loginMember == null || loginMember.getMemberNo() != board.getMemberNo()) {
				
				// 요청에 담긴 모든 쿠키 얻어오기
				// (import 주의) jakarta.servlet.http.Cookie
				Cookie[] cookies = req.getCookies();
				Cookie c = null;
				
				// 요청에 담긴 쿠키에 "readBoardNo"가 존재할 때 c 에 temp 대입하고 for문 탈출
				for(Cookie temp : cookies) {
					if(temp.getName().equals("readBoardNo")) {
						c = temp;
						break;
					}
				}
				
				int result = 0;		// 조회수 증가 결과를 저장할 변수
				
				if(c == null) {		// "readBoardNo"가 쿠키에 없을 때
					// 새 쿠키 생성("readBoardNo", [게시글 번호])
					c = new Cookie("readBoardNo", "[" + boardNo +"]");
					result = service.updateReadCount(boardNo);
					
				} else {	// "readBoardNo"가 쿠키에 있을 때 ex) "readBoardNo" : [2][30][200][2001]
					
					// 현재 글을 처음 읽는 경우
					if(c.getValue().indexOf("[" + boardNo +"]") == -1) {
						c.setValue(c.getValue() + "[" + boardNo +"]");
						result = service.updateReadCount(boardNo);
					}
					
				}
				
				// 조회 수 증가 성공 / 조회 성공 시
				if(result > 0) {
					
					// 먼저 조회된 board 의 readCount 값을 result 값으로 다시 세팅
					board.setReadCount(result);
					
					// 쿠키 적용 경로 설정
					c.setPath("/");		// "/" 이하 경로 요청 시 쿠키를 서버로 전달
					
					// 쿠키 수명 지정
					LocalDateTime now = LocalDateTime.now();	// 현재 시간
					LocalDateTime nextDayMidnight = now.plusDays(1)		// 다음날 자정 지정
							.withHour(0).withMinute(0).withSecond(0).withNano(0);
					// 다음날 자정까지 남은 시간 계산 (초 단위) (import java.time.Duration)
					long secondsUntilNextDay = Duration.between(now, nextDayMidnight).getSeconds();
					
					// 쿠키 수명 설정
					c.setMaxAge((int)secondsUntilNextDay);
					
					// 응답객체(resp)를 이용해 클라이언트에게 전달
					resp.addCookie(c);
				}	
			}
			/* ----- 쿠키 이용한 조회수 증가 여기까지 ----- */
			
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
	
	/** 게시글 좋아요 체크/해제
	 * @param map : loginMemberNo, boardNo, likeCheck
	 * @return count
	 */
	@ResponseBody
	@PostMapping("like")
	public int boardLike(@RequestBody Map<String, Integer> map) {
		return service.boardLike(map);
	}
}
