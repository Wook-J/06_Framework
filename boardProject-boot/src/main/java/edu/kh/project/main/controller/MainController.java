package edu.kh.project.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MainController {
	
	@RequestMapping("/")	// "/" 요청 매핑
	public String mainPage() {
		// 접두사(classpath:/templates/), 접미사(.html)
		return "common/main";
	}
	
	@GetMapping("test")
	public String testPage() {
		return "common/test";
	}
	
//	@RequestMapping("/")	// "/" 요청 매핑
//	public String mainPage() {
//		// 접두사(classpath:/templates/), 접미사(.html)
//		return "index";
//	}

	// loginFilter -> loginError 리다이렉트
	// -> message 만들어서 메인페이지로 리다이렉트
	@GetMapping("loginError")
	public String loginError(RedirectAttributes ra) {
		ra.addFlashAttribute("message","로그인 후 이용해 주세요~");
		return "redirect:/";
	}
}
