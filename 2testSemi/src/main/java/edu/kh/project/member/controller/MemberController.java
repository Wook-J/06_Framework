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

@Controller
@RequestMapping("member")
@SessionAttributes({"loginMember"})
@Slf4j
public class MemberController {

	@Autowired
	private MemberService service;
	
	/* *************** 단순 페이지 보여주는 경우 *************** */
	
	@GetMapping("login")
	public String loginPage() {
		return "member/login";
	}
	
	@GetMapping("signup")
	public String signupPage() {
		return "member/signup";
	}
	
	@GetMapping("findEmail")
	public String findEmailPage() {
		return "member/findEmail";
	}
	
	@GetMapping("findPw")
	public String findPwPage() {
		return "member/findPw";
	}
	
	/* *************** 로그인 *************** */
	@PostMapping("login")
	public String login(Member inputMember,
						@RequestParam(value="saveId", required = false) String saveId,
						Model model, HttpServletResponse resp,
						RedirectAttributes ra) {
		
		Member loginMember = service.login(inputMember);
		
		if(loginMember == null) ra.addFlashAttribute("message", "아이디 또는 비밀번호가 일치하지 않습니다");
		else {
			model.addAttribute("loginMember", loginMember);
			Cookie cookie = new Cookie("saveId", loginMember.getMemberEmail());
			cookie.setPath("/");
			
			if(saveId != null) cookie.setMaxAge(60*60*24*30);	// 30일(초단위)
			else cookie.setMaxAge(0);
			
			resp.addCookie(cookie);
		}
		
		return "redirect:/";
	}
	
	/* *************** 로그아웃 *************** */
	@GetMapping("logout")
	public String logout(SessionStatus status) {
		status.setComplete();
		return "redirect:/";
	}

	/* *************** 회원가입 *************** */
	/** 이메일 중복검사(비동기)
	 * @param memberEmail
	 * @return
	 */
	@ResponseBody
	@GetMapping("checkEmail")
	public int checkEmail(@RequestParam("memberEmail") String memberEmail) {
		return service.checkEmail(memberEmail);
	}
	
	/** 닉네임 중복검사(비동기)
	 * @param memberNickname
	 * @return
	 */
	@ResponseBody
	@GetMapping("checkNickname")
	public int checkNickname(@RequestParam("memberNickname") String memberNickname) {
		return service.checkNickname(memberNickname);
	}
	
	/** 회원가입
	 * @param inputMember
	 * @param memberAddress
	 * @param ra
	 * @return
	 */
	@PostMapping("signup")
	public String singup(Member inputMember,
						@RequestParam("memberAddress") String[] memberAddress,
						RedirectAttributes ra) {
		
		int result = service.signup(inputMember, memberAddress);
		String path = null;
		String message = null;
		
		if(result > 0) {
			message = inputMember.getMemberNickname() + "님의 가입을 환영합니다!";
			path = "/";
		}else {
			message = "회원가입 실패...";
			path = "signup";
		}
		
		ra.addFlashAttribute("message", message);
		return "redirect:" + path;
	}
	
}
