package edu.kh.project.myPage.model.service;

import java.io.File;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.mapper.MyPageMapper;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor=Exception.class)		// 모든 예외 발생 시 롤백 or 커밋
@RequiredArgsConstructor		// 필드에 private final 변수에 대해 DI 해 줌
public class MyPageServiceImpl implements MyPageService{

	private final MyPageMapper mapper;
	private final BCryptPasswordEncoder bcrypt;		// SecurityConfig.java 참고
	
	/** 회원정보 수정 전 닉네님 중복검사 서비스
	 * 망했어ㅜㅜ loginMember의 닉네임이 여기서 바뀌는군.....
	 */
	@Override
	public int updateInfoDuplication(Member loginMember, Member updateMember) {
		if(loginMember.getMemberNickname().equals(updateMember.getMemberNickname())) return 0;
		loginMember.setMemberNickname(updateMember.getMemberNickname());
		return mapper.updateInfoDuplication(loginMember);
	}
	
	/** 회원정보 수정 서비스 내버전 (3개 전달해야 해서 별로...??)
	 * updateMember : 수정할 회원 정보(memberNickname, memberTel, memberAddress)
	 * memberAddress : 별도로 수정할 주소 형태
	 */
	@Override
	public int updateInfo(Member loginMember, Member updateMember, String[] memberAddress) {
		
		if(!updateMember.getMemberAddress().equals(",,")) {
			String address = String.join("^^^", memberAddress);
			loginMember.setMemberAddress(address);
			
		} else loginMember.setMemberAddress(null);
		
		loginMember.setMemberNickname(updateMember.getMemberNickname());
		loginMember.setMemberTel(updateMember.getMemberTel());
		
		return 0;
	}

	@Override	// 회원정보 수정 서비스, 선생님 버전
	public int updateInfo(Member updateMember, String[] memberAddress) {
		
		// 입력된 주소가 있을 경우 memberAddress를 A^^^B^^^C 형태로 가공
		// 주소 입력 X 시 -> updateMember.getMemberAddress() == ",,"
		if(updateMember.getMemberAddress().equals(",,")) {
			updateMember.setMemberAddress(null);
			
		} else {
			String address = String.join("^^^", memberAddress);
			updateMember.setMemberAddress(address);
		}
		
		return mapper.updateInfo(updateMember);
	}

	@Override	// 회원 비밀번호 변경 서비스
	public int changePw(int memberNo, String currentPw, String newPw) {
		
		String memberPw = mapper.memberPw(memberNo);
		
		if(!bcrypt.matches(currentPw, memberPw)) return 0;
		
		String encPw = bcrypt.encode(newPw);
		
		Member member = new Member();		
		member.setMemberNo(memberNo);
		member.setMemberPw(encPw);

		// mapper 에는 변수 1개만 전할 수 있음.....
		return mapper.changePw(member);
	}

	@Override	// 회원 비밀번호 변경 서비스 (선생님 버전)
	public int changePwByTeacher(Map<String, Object> paramMap, int memberNo) {
		
		// BCryptPasswordEncoder bcrypt 는 서비스단에서 이용할 수 있어서 기존 DB 가져와야함
		String originPw = mapper.selectPw(memberNo);
		
		// early return 코드를 먼저 작성 (불필요한 연산을 제외하려고)
		if(!bcrypt.matches((String)paramMap.get("currentPw"), originPw)) return 0;
		
		String encPw = bcrypt.encode((String)paramMap.get("newPw"));
		
		// myBatis에서 SQL에 전달할 수 있는 인자는 1개 뿐 -> 묶어서 전달(parmaMap, 기존 꺼 사용)
		paramMap.put("encPw", encPw);
		paramMap.put("memberNo", memberNo);
		
		return mapper.changePwByTeacher(paramMap);
	}

	@Override	// 회원 탈퇴 서비스
	public int secession(String memberPw, int memberNo) {
		
		// 현재 로그인한 회원의 암호화된 비밀번호를 DB에서 조회
		String originPw = mapper.selectPw(memberNo);
		
		// 비밀번호가 다를 경우 early return 코드
		if(!bcrypt.matches(memberPw, originPw)) return 0;
		
		return mapper.secession(memberNo);
	}

	@Override	// 파일 업로드 테스트1
	public String fileUpload1(MultipartFile uploadFile) throws Exception{
		
		// MultipartFile 이 제공하는 메서드
		// - getSize() : 파일의 크기
		// - isEmpty() : 업로드한 파일이 없으면 true, 있으면 false
		// - getOriginalFileName() :원본 파일명
		// - transferTo(경로) : 메모리 또는 임시 저장경로에 업로드된 파일을
		//       원하는 경로에 실제로 전송(서버에 어떤 폴더에 저장할 지 지정)
		
		// 업로드한 파일이 없는 경우
		if(uploadFile.isEmpty()) return null;	
		
		// 업로드한 파일이 있을 경우
		// C:/uploadFiles/test/파일명 으로 서버에 저장
		uploadFile.transferTo(new File("C:/uploadFiles/test/" + uploadFile.getOriginalFilename()));
		
		// 웹에서 해당 파일에 접근할 수 있는 경로를 반환
		
		// 서버 : C:/uploadFiles/test/A.jpg
		// 웹 접근주소 : /myPage/file/A.jpg
		
		return "/myPage/file/" + uploadFile.getOriginalFilename();
	}


}
