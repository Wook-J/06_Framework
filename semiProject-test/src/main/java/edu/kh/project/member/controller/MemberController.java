package edu.kh.project.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Controller
@SessionAttributes({"loginMember"})
@RequestMapping("member")
@Slf4j
public class MemberController {
	
	@Autowired
	private MemberService service;

	@PostMapping("login")
	public String login(Member inputMember,
			RedirectAttributes ra,
			Model model,
			@RequestParam(value="saveId", required = false) String saveId,	// 아이디 저장여부
			HttpServletResponse resp) {
		
		Member loginMember = service.login(inputMember);
		
		if(loginMember == null) ra.addFlashAttribute("message", "아이디 또는 비밀빈호 불일치");
		else {
			model.addAttribute("loginMember", loginMember);
			Cookie cookie = new Cookie("saveId", loginMember.getMemberEmail());
			cookie.setPath("/");
			
			// 쿠키의 만료기간 지정 (아이디 저장 체크여부)
			if(saveId != null) cookie.setMaxAge(60*60*24*30);	// 30일(초단위)로 지정
			else cookie.setMaxAge(0);
			
			resp.addCookie(cookie);
		}
		
		return "redirect:/";
	}
	
	@GetMapping("logout")
	public String logout(SessionStatus status) {
		status.setComplete();
		return "redirect:/";
	}
	
	@GetMapping("signup")
	public String signupPage() {
		return "member/signup";
	}
	
}
