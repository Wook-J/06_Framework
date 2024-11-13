package edu.kh.project.myPage.model.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.common.util.Utility;
import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.dto.UploadFile;
import edu.kh.project.myPage.model.mapper.MyPageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(rollbackFor=Exception.class)		// 모든 예외 발생 시 롤백 or 커밋
@RequiredArgsConstructor		// 필드에 private final 변수에 대해 DI 해 줌
@Slf4j
@PropertySource("classpath:/config.properties")
public class MyPageServiceImpl implements MyPageService{

	private final MyPageMapper mapper;
	private final BCryptPasswordEncoder bcrypt;		// SecurityConfig.java 참고
	
	// -------------------------------------------------------------------
	@Value("${my.profile.web-path}")
	private String profileWebPath;
	
	@Value("${my.profile.folder-path}")
	private String profileFolderPath;
	
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

	@Override	// 파일 업로드 테스트2 (+DB에 저장)
	public int fileUpload2(MultipartFile uploadFile, int memberNo) throws Exception {
		
		// 업로드한 파일이 없는 경우 == 선택된 파일이 없는 경우
		if(uploadFile.isEmpty()) return 0;
		
		/* DB에 파일 저장이 가능은 하지만 DB부하를 줄이기 위해
		 * 
		 * 1) DB 에는 서버에 저장할 파일 경로를 저장
		 * 
		 * 2) DB 삽입/수정 성공 후 서버에 파일을 저장
		 * 
		 * 3) 만약 파일 저장 실패 시 예외 발생
		 *    -> @Transactional 을 이용해서 rollback 수행(class 상단에 적혀있음)
		 * */
		
		// 1. 서버에 저장할 파일 경로 만들기
		// - 파일이 실제로 저장될 서버의 폴더 경로
		String folderPath = "C:/uploadFiles/test/";
		
		// - 클라이언트가 파일이 저장된 폴더에 접근할 수 있는 주소(정적 리소스 요청 주소)
		String webPath = "/myPage/file/";
		
		
		// 2. DB에 전달할 데이터를 DTO로 묶어서 INSERT 호출하기
		// webPath, memberNo, 원본 파일명, 변경된 파일명
		String fileRename = Utility.fileRename(uploadFile.getOriginalFilename());
		
//		log.debug("fileRename : " + fileRename);	// 확인용
		
		// Builder 패턴 이용헤서 UploadFile 객체 생성
		// 장점 1) 반복되는 참조변수명, set 구문 생략
		// 장점 2) method chaining 을 이용하여 한 줄로 작성 가능
		UploadFile uf = UploadFile.builder()
						.memberNo(memberNo)
						.filePath(webPath)
						.fileOriginalName(uploadFile.getOriginalFilename())
						.fileRename(fileRename)
						.build();
		
		int result = mapper.insertUploadFile(uf);
		
		// 3. 삽입(INSERT) 성공 시 파일을 지정된 서버 폴더에 저장
		
		// 삽입 실패 시 early return
		if(result == 0) return 0;
		
		//삽입 성공시 C:/uploadFiles/test/변경된 파일명 으로 파일을 서버 컴퓨터에 저장
		uploadFile.transferTo(new File(folderPath + fileRename));
		
		return result;
	}
	
	@Override	// 파일 목록 조회 서비스
	public List<UploadFile> fileList(int memberNo) {
		return mapper.fileList(memberNo);
	}
	
	@Override	// 여러 파일 업로드 서비스
	public int fileUpload3(List<MultipartFile> aaaList,
						List<MultipartFile> bbbList,
						int memberNo) throws Exception{
		
		// 1. aaaList 처리
		int result1 = 0;
		
		for(MultipartFile file : aaaList) {	// 업로드된 파일이 없을 경우를 제외하고 업로드
			
			if(file.isEmpty()) continue;	// 파일이 없으면 다음 파일로 넘어가기
			
			result1 += fileUpload2(file, memberNo);	// fileUpload2() 메서드 호출(재활용)
		}
		
		// 2. bbbList 처리
		int result2 = 0;
		
		for(MultipartFile file : bbbList) {	// 업로드된 파일이 없을 경우를 제외하고 업로드
			
			if(file.isEmpty()) continue;	// 파일이 없으면 다음 파일로 넘어가기
			
			result2 += fileUpload2(file, memberNo);	// fileUpload2() 메서드 호출(재활용)
		}
		
		return result1 + result2;
	}

	@Override	// 프로필 이미지 수정 서비스
	public int profile(MultipartFile profileImg, Member loginMember) throws Exception {
		
		String updatePath = null;	// 프로필 이미지 경로 (DB에 수정할 경로)
		String rename = null;		// 변경명 저장
		
		// 업로드한 이미지가 있을 경우
		// -> 경로 조합(클라이언트 접근 경로 + rename 한 파일명)
		if(!profileImg.isEmpty()) {
			// updatePath 에 들어갈 경로 조합
			rename = Utility.fileRename(profileImg.getOriginalFilename());
			updatePath = profileWebPath + rename;
		}
		
		// 수정된 프로필 이미지 경로 + 회원 번호를 저장할 DTO 객체
		Member mem = Member.builder()
					.memberNo(loginMember.getMemberNo())
					.profileImg(updatePath)
					.build();
		
		// UPDATE 수행
		int result = mapper.profile(mem);
		
		if(result > 0) {	// DB에서 수정 성공
			
			// 프로필 이미지를 없앤 경우(NULL로 수정한 경우)를 제외
			// -> 업로드한 이미지가 있을 경우에만 
			if(!profileImg.isEmpty()) {
				
				// 파일을 서버에 지정된 폴더에 저장 C:/uploadFiles/profile/변경한이름
				profileImg.transferTo(new File(profileFolderPath + rename));
			}
			
			// 세션 회원정보에서 프로필 이미지 경로를 업데이트한 경로로 변경
			loginMember.setProfileImg(updatePath);
		}
		
		return result;
	}
}
