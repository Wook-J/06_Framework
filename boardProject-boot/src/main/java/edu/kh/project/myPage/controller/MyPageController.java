package edu.kh.project.myPage.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.dto.UploadFile;
import edu.kh.project.myPage.model.service.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/* @SessionAttributes( {"key", "key", "key", ...} ) 의 역할
 * - Model 에 추가된 속성 중 key 값이 일치하는 속성을 session scope 로 변경
 * - SessionStatus 이용 시 session 에 등록된 완료할 대상을 찾는 용도
 * */
@Controller
@RequestMapping("myPage")
@RequiredArgsConstructor		// @Autowired 안하고 필드명에 final 붙이면 DI 해줌!!
@SessionAttributes({"loginMember"})
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
	public String profilePage() {
		return "myPage/myPage-profile";
	}
	
	@GetMapping("changePw")		// 비밀번호 변경 화면 이동
	public String changePwPage() {
		return "myPage/myPage-changePw";
	}
	
	@GetMapping("secession")	// 회원 탈퇴 화면 이동
	public String secessionPage() {
		return "myPage/myPage-secession";
	}
	
	@GetMapping("fileTest")		// 파일 업로드 테스트 화면 이동
	public String fileTestPage() {
		return "myPage/myPage-fileTest";
	}
	
	/** 파일 목록 조회하여 응답화면으로 이동
	 * @param loginMember : 현재 로그인한 회원의 정보
	 * @param model : 값 전달용 객체(기본 request scope)
	 * @return
	 */
	@GetMapping("fileList")
	public String fileListPage(@SessionAttribute("loginMember") Member loginMember,
							Model model) {
		
		// 파일 목록 조회 서비스 호출 (현재 로그인한 회원이 올린 이미지만 조회)
		int memberNo = loginMember.getMemberNo();
		List<UploadFile> list = service.fileList(memberNo);
		model.addAttribute("list", list);
		
		// classpath:templates/myPage/myPage-fileList.html 로 forward
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
//			message = "비밀번호가 변경되었습니다. 변경된 비밀번호로 로그인해 주세요.";
			
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
	
	
	/** 비밀번호 변경
	 * @param loginMember
	 * @param currentPw
	 * @param newPw
	 * @param ra
	 * @return
	 */
	@PostMapping("changePw")	// /myPage/changePw POST 요청 매핑
	public String changePw(@SessionAttribute("loginMember") Member loginMember,
							@RequestParam("currentPw") String currentPw,
							@RequestParam("newPw") String newPw,
							RedirectAttributes ra) {
		
		int result = service.changePw(loginMember.getMemberNo(), currentPw, newPw);
		
		String message;
		String url;
		
		if(result > 0) {
			message = "비밀번호가 변경되었습니다";
			url = "redirect:info";
			
		} else {
			message = "현재 비밀번호가 일치하지 않습니다";
			url = "redirect:changePw";
		}
		
		ra.addFlashAttribute("message", message);
		
		return url;
	}
	
	/** 비밀번호 변경(선생님 버전)
	 * @param paramMap : 모든 파라미터를 맵으로 저장
	 * @param loginMember : 세션에 등록된 현재 로그인한 회원 정보
	 * @param ra : 리다이렉트시 request scope 로 메세지 전달하는 역할
	 * @return
	 */
	@PostMapping("changePwByTeacher")
	public String changePwByTeacher(@RequestParam Map<String, Object> paramMap,
					@SessionAttribute("loginMember") Member loginMember,
					RedirectAttributes ra) {
		
//		log.debug("paramMap : " + paramMap);		// 확인용
//		log.debug("loginMember : " + loginMember);	// 확인용
		
		int memberNo = loginMember.getMemberNo();
		
		int result = service.changePwByTeacher(paramMap, memberNo);
		
		String path = null;
		String message = null;
		
		if(result > 0) {
			message = "비밀번호가 변경되었습니다";
			path = "/myPage/info";
			
		} else {
			message = "현재 비밀번호가 일치하지 않습니다";
			path = "/myPage/changePw";
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:" + path;
	}
	
	/**
	 * @param loginMember : 로그인한 회원 정보 (세션)
	 * @param memberPw : 입력받은 비밀번호
	 * @param status : 세션 완료용도의 객체
	 * 			-> 클래스 상단에 @SessionAttributes 로 등록된 세션을 완료
	 * @return
	 */
	@PostMapping("secession")
	public String secession(@SessionAttribute("loginMember") Member loginMember,
						@RequestParam("memberPw") String memberPw,
						SessionStatus status,
						RedirectAttributes ra) {
		
		// 로그인한 회원의 회원번호 꺼내기
		int memberNo = loginMember.getMemberNo();
		
		// 서비스 호출 (입력받은 비밀번호, 로그인한 회원번호)
		int result = service.secession(memberPw, memberNo);
		
		String message = null;
		String path = null;
		
		if(result > 0) {
			message = "탈퇴 되었습니다.";
			path = "/";
			status.setComplete();
			
		} else {
			message = "비밀번호가 일치하지 않습니다.";
			path = "secession";
		}
		
		ra.addFlashAttribute("message", message);
		
		// 탈퇴 성공 - redirect:/ (메인페이지 재요청)
		// 탈퇴 실패 - redirect:secession -> /myPage/secession(GET 요청)
		return "redirect:" + path;
	}
	
	
	/* Spring 에서 파일 업로드를 처리하는 방법
	 * 
	 * - HTML에서 enctype="multipart/form-data"로 클라이언트 요청을 받으면
	 *   (문자, 숫자, 파일 등이 섞여있는 요청)
	 *   
	 *   이를 MultipartResolver(FileConfig 에 정의)를 이용해서 섞여 있는 파라미터를 분리
	 *   
	 *   문자열, 숫자 -> String
	 *   파일		  -> MultipartFile
	 * 
	 * */
	/** 파일테스트 1
	 * @param uploadFile : 업로드한 파일 + 파일에 대한 내용 및 설정 내용
	 * @return
	 */
	@PostMapping("file/test1")		// /myPage/file/test1 POST 요청 매핑
	public String fileUpload1(@RequestParam("uploadFile") MultipartFile uploadFile,
							RedirectAttributes ra) throws Exception{
		
		String path = service.fileUpload1(uploadFile);
		
		// 파일이 저장되어 웹에서 접근할 수 있는 경로가 반환되었을 때
		if(path != null) {
			ra.addFlashAttribute("path", path);
		}
		
		return "redirect:/myPage/fileTest";
	}
	
	@PostMapping("file/test2")
	public String fileUpload2(@RequestParam("uploadFile") MultipartFile uploadFile,
							@SessionAttribute("loginMember") Member loginMember,
							RedirectAttributes ra) throws Exception {
		
		// 로그인한 회원의 번호 얻어오기 (누가 업로드 했는가)
		int memberNo = loginMember.getMemberNo();
		
		// 업로드된 파일 정보를 DB에 INSERT 후 결과 행의 개수 반환 받을 예정
		int result = service.fileUpload2(uploadFile, memberNo);
		
		String message = null;
		
		if(result > 0) message = "파일 업로드 성공";
		else message = "파일 업로드 실패...";
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:/myPage/fileTest";		// /myPage/fileTest GET 방식 재요청
	}

	@PostMapping("file/test3")
	public String fileUpload3(@SessionAttribute("loginMember") Member loginMember,
							@RequestParam("aaa") List<MultipartFile> aaaList,
							@RequestParam("bbb") List<MultipartFile> bbbList,
							RedirectAttributes ra) throws Exception {
		
		// aaa : 파일 미제출 시 0, 1번 인덱스 파일이 비어있는 상태로 넘어옴(input 태그 2개)
		// bbb : 파일 미제출 시 0번 인덱스 파일이 비어있는 상태로 넘어옴(input 태그 1개, multiple 사용)
		
//		log.debug("aaaList : " + aaaList);
//		log.debug("bbbList : " + bbbList);
		
		// 여러 파일 업로드 서비스 호출 (result : 업로드된 파일 개수)
		int memberNo = loginMember.getMemberNo();
		int result = service.fileUpload3(aaaList, bbbList, memberNo);
		
		String message = null;
		if(result == 0 ) message = "업르도된 파일이 없습니다";
		else message = result + "개의 파일이 업로드 되었습니다!";
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:/myPage/fileTest";
	}
	
	/** 프로필 이미지 변경 요청
	 * @param loginMember
	 * @param profileImg
	 * @param ra
	 * @return
	 */
	@PostMapping("profile")
	public String profile(@SessionAttribute("loginMember") Member loginMember,
						@RequestParam("profileImg") MultipartFile profileImg,
						RedirectAttributes ra) throws Exception {
		
		int result = service.profile(profileImg, loginMember);
		
		String message = null;
		if(result > 0) message = "프로필 변경 성공!";
		else message = "프로필 변경 실패ㅠㅠ";
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:profile";	// /myPage/profile 로 GET 요청(상대경로)
	}
}
