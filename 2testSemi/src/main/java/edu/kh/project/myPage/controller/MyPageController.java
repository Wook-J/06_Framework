package edu.kh.project.myPage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.service.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("myPage")
@SessionAttributes({"loginMember"})
@RequiredArgsConstructor
@Slf4j
public class MyPageController {
	
	private final MyPageService service;
	
	/* *************** 단순 페이지 보여주는 경우 *************** */
	
	@GetMapping("info")
	public String infoPage() {
		return "myPage/myPage-info";
	}
	
	@GetMapping("update")
	public String updatePage(@SessionAttribute("loginMember") Member loginMember,
							Model model) {
		
		String memberAddress = loginMember.getMemberAddress();
		if(memberAddress != null) {
			String[] arr = memberAddress.split("\\^\\^\\^");
			model.addAttribute("postcode", arr[0]);
			model.addAttribute("address", arr[1]);
			model.addAttribute("detailAddress", arr[2]);
		}
		
		return "myPage/myPage-update";
	}
	
	@GetMapping("changePw")
	public String changePwPage() {
		return "myPage/myPage-changePw";
	}
	
	@GetMapping("secessionPage")
	public String secessionPage() {
		return "myPage/myPage-secession";
	}
	
	
	/* *************** 내 정보 수정 *************** */
	@PostMapping("update")
	public String update(@SessionAttribute("loginMember") Member loginMember,
						Member updateMember,
						@RequestParam("memberAddress") String[] memberAddress,
						RedirectAttributes ra) {
		
		updateMember.setMemberNo(loginMember.getMemberNo());
		int result = service.updateInfo(updateMember, memberAddress);
		
		return "redirect:info";
	}
	
	
	/* *************** 비밀번호 변경 *************** */
	
	
	/* *************** 회원 탈퇴 *************** */

}
