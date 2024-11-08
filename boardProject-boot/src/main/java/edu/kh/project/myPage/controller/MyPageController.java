package edu.kh.project.myPage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.service.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("myPage")
@RequiredArgsConstructor		// @Autowired 안하고 필드명에 final 붙이면 DI 해줌!!
@Slf4j
public class MyPageController {

	private final MyPageService service;
	
	/**
	 * @param loginMember : 세션에 존재하는 loginMember 를 얻어와 매개변수에 대입
	 * @return
	 */
	@GetMapping("info")			// 마이페이지 기본정보 화면 이동
	public String info(@SessionAttribute("loginMember") Member loginMember,
					Model model) {
		
		// 현재 로그인한 회원의 주소를 꺼내옴
		// 현재 로그인한 회원 정보는 세션에 등록된 상태(loginMember)
		log.debug("loginMember" + loginMember);
		
		String memberAddress = loginMember.getMemberAddress();
		
		if(memberAddress != null) {
			// 구분자 "^^^" 를 기준으로 memberAddress를 쪼개어 String[] 로 반환
			String[] arr = memberAddress.split("\\^\\^\\^");
//			log.debug("arr : " + arr);
			model.addAttribute("postcode", arr[0]);
			model.addAttribute("address", arr[1]);
			model.addAttribute("detailAddress", arr[2]);
		}
		
		return "myPage/myPage-info";
	}
	
	@GetMapping("profile")		// 프로필 이미지 변경 화면 이동
	public String profile() {
		return "myPage/myPage-profile";
	}
	
	@GetMapping("changePw")		// 비밀번호 변경 화면 이동
	public String changePw() {
		return "myPage/myPage-changePw";
	}
	
	@GetMapping("secession")	// 회원 탈퇴 화면 이동
	public String secession() {
		return "myPage/myPage-secession";
	}
	
	@GetMapping("fileTest")		// 파일 업로드 테스트 화면 이동
	public String fileTest() {
		return "myPage/myPage-fileTest";
	}
	
	@GetMapping("fileList")		// 파일 목록 조회 화면 이동
	public String fileList() {
		return "myPage/myPage-fileList";
	}
	
	/** 회원정보 수정
	 * @param loginMember : 로그인한 회원 정보 (회원 번호 사용할 예정)
	 * @param updateMember : 커맨드 객체(@ModelAttribute 생략된 상태) 닉네임, 전화번호, 주소(문자열)
	 * @param memberAddress : 주소만 따로 받은 String[]
	 * @param ra : 리다이렉트 시 request scope 로 message 와 같은 데이터 전달 
	 * @return : redirect:info (상대주소)
	 */
	@PostMapping("info")
	public String updateInfo(@SessionAttribute("loginMember") Member loginMember,
							Member updateMember,
							@RequestParam("memberAddress") String[] memberAddress,
							RedirectAttributes ra) {
		
		/* 망했어... MemberServiceImple에서 문제발생!		
		int resultDuplication = service.updateInfoDuplication(loginMember, updateMember);
		
		if(resultDuplication > 0) {		// 중복되는 놈 있는 경우
			message = "동일한 닉네임을 가진 회원이 있습니다";
			ra.addFlashAttribute("message", message);
			return "redirect:info";
		}
 		*/
		
		// 선생님 버전 : 여기서 memberNo 가져와서 service 호출
		// 회원 닉네임, 전화번호, 주소, 회원번호
		updateMember.setMemberNo(loginMember.getMemberNo());
		int result = service.updateInfo(updateMember, memberAddress);
		
		// 내버전 : 일단 보내고 service 에서 해결
//		int result = service.updateInfo(loginMember, updateMember, memberAddress);
		
		String message = null;
		
		if(result > 0) {
			message = "회원 정보 수정 성공!!";
			
			// loginMember 새로 세팅 (우리가 방금 바꾼 값으로)
			// -> loginMember는 세션에 저장된 로그인한 회원 정보가 저장된 객체를 참조하고 있음!!
			// -> loginMember를 수정하면 세션에 저장된 로그인한 회원정보가 수정된다
			// == 세션 데이터와 DB데이터를 맞춤
			loginMember.setMemberNickname(updateMember.getMemberNickname());
			loginMember.setMemberTel(updateMember.getMemberTel());
			loginMember.setMemberAddress(updateMember.getMemberAddress());
		}
		else message = "회원 정보 수정 실패..";
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:info";
	}

}
