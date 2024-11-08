package edu.kh.project.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/* @SessionAttributes( {"key", "key", "key", ...} )
 * - Model 에 추가된 속성 중 key 값이
 *   일치하는 속성을 session scope 로 변경
 * */

@SessionAttributes({"loginMember"})
@Controller			// 제어반전(IOC)
@Slf4j
@RequestMapping("member")
public class MemberController {

	@Autowired		// 의존성 주입(DI)
	private MemberService service;
	
	/* [로그인]
	 * - 특정 사이트에 아이디/비밀번호 등을 입력해서
	 *   해당 정보가 있으면 조회/서비스 이용
	 * 
	 * - 로그인 한 회원 정보를 session 에 기록하여
	 *   로그아웃 또는 브라우저 종료(탭 종료X) 시 까지 
	 *   해당 정보를 계속 이용할 수 있게 함
	 * 
	 * */
	/** 로그인
	 * @param inputMember : 커맨드 객체 (@ModelAttribute 생략)
	 * 						memberEmail. memberPw 가 세팅된 상태
	 * @param ra : 리다이렉트 시 request scope 로 데이터 전달하는 객체(request -> session -> request)
	 * @param model : 데이터 전달용 객체 (기본 request scope)
	 * 				  (@SessionAttributes 어노테이션과 함께 사용시 session scope 이동)
	 * @param saveId
	 * @param resp
	 * @return
	 */
	@PostMapping("login")
	public String login(Member inputMember,
					RedirectAttributes ra,
					Model model,
					@RequestParam(value="saveId", required=false) String saveId,
					HttpServletResponse resp) {
		
		// 체크박스
		// - 체크가 된 경우 : "on"
		// - 체크가 안 된 경우 : null
		
		// 로그인 서비스 호출
		Member loginMember = service.login(inputMember);
		
		if(loginMember == null) {
			ra.addFlashAttribute("message", "아이디 또는 비밀번호가 일치하지 않습니다");
		} else {
			// Session scope 에 loginMember 추가
			// 1단계 : request scope 에 세팅
			model.addAttribute("loginMember", loginMember);
			
			// 2단계 : 클래스 위에 @SessionAttributes() 작성하여 session scope 로 이동
			// 		   클래스 상단에 어노테이션 참고!!!
			
			// **************** Cookie ******************
			// 이메일 저장
			
			// 쿠키 객체 생성(K:V)
			// import jakarta.servlet.http.Cookie;
			Cookie cookie = new Cookie("saveId", loginMember.getMemberEmail());
			// 내부 : saveId=user01@kh.or.kr
			
			// 쿠키가 적용될 경로 설정
			// -> 클라이언트가 어떤 요청을 할 때 쿠키가 첨부될 지 지정
			
			// ex) "/" : IP 또는 도메인 또는 localhost
			//			--> 메인페이지 + 그 하위 주소 모두
			cookie.setPath("/");
			
			// 쿠키의 만료기간 지정 (아이디 저장 체크여부)
			if(saveId != null) cookie.setMaxAge(60*60*24*30);	// 30일(초단위)로 지정
			else cookie.setMaxAge(0);
			
			// 응답 객체에 쿠키 추가 -> 클라이언트 쪽으로 전달
			resp.addCookie(cookie);
		}
		
		return "redirect:/";	// 메인페이지 재요청
	}
	
	/** 로그아웃 : session 에 저장된 로그인된 회원 정보를 없앰
	 * @param SessionStatus : @SessionAttributes 로 지정된 특정 속성을 
	 * 						  세션에서 제거하는 기능을 제공하는 객체
	 * @return
	 */
	@GetMapping("logout")
	public String logout(SessionStatus status) {
		// 세션을 완료시킴 (== 세션에서 @SessionAttributes로 등록된 세션 제거)
		status.setComplete();
		
		return "redirect:/";
	}
	
	/** 회원 가입 페이지로 이동
	 * @return
	 */
	@GetMapping("signup")
	public String signupPage() {
		return "member/signup";
	}
	
	/** 이메일 중복검사 
	 * @return
	 */
	@ResponseBody		// 응답 본문(fetch)으로 돌려보냄
	@GetMapping("checkEmail")	// GET요청 /member/checkEmail
	public int checkEmail(@RequestParam("memberEmail") String memberEmail) {
		return service.checkEmail(memberEmail);
	}
	
	/** 닉네임 중복검사
	 * @param memberNickname
	 * @return 중복이면 1 , 아니면 0
	 */
	@ResponseBody
	@GetMapping("checkNickname")
	public int checkNickname(@RequestParam("memberNickname") String memberNickname) {
		return service.checkNickname(memberNickname);
	}
	
	/** 회원 가입
	 * @param inputMember : 입력된 회원정보(memberEmail, memberPw, memberNickname, memberTel,
	 * 						memberAddress -> 따로 처리)
	 * @param memberAddress : 입력한 주소 input 3개의 값(배열) [우편번호, 도로명/지번주소, 상세주소]
	 * @param ra : 리다이렉트 시 request scope 로 데이터 전달하는 객체
	 * @return
	 */
	@PostMapping("signup")		// 동기식 POST 요청
	public String signup(Member inputMember,
					@RequestParam("memberAddress") String[] memberAddress,
					RedirectAttributes ra) {
		
//		log.debug("inputMember : " + inputMember);
		
		// 회원가입 서비스 호출
		int result = service.signup(inputMember, memberAddress);
		
		String path = null;
		String message = null;
		
		if(result > 0) {
			message = inputMember.getMemberNickname() + "님의 가입을 환영합니다!";
			path = "/";
			
		} else {
			message = "회원 가입 실패...";
			path = "signup";
			
		}
		
		ra.addFlashAttribute("message", message);

		return "redirect:" + path;
		// 성공 -> redirect:/
		// 실패 -> redirect:signup (/ 없어서 상대경로임) -> /member/signup 으로 GET요청
	}
}
