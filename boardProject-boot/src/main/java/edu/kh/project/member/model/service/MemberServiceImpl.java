package edu.kh.project.member.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.mapper.MemberMapper;
import lombok.extern.slf4j.Slf4j;

@Transactional(rollbackFor=Exception.class)
@Service
@Slf4j
public class MemberServiceImpl implements MemberService{
	
	// 등록된 Bean 중 같은 타입 or 상속관계인 Bean
	@Autowired		// 의존성 주입(DI)
	private MemberMapper mapper;
	
	// Bcrypt 암호화 객체 의존성 주입(SecurityConfig 참고)
	@Autowired
	private BCryptPasswordEncoder bcrypt;

	@Override		// 로그인 서비스
	public Member login(Member inputMember) {

		// 암호화 진행 (bcrypt라는 암호화 패턴)
		
		// bcrypt.encode(문자열) : 문자열을 암호화하여 반환
//		String bcryptPassword = bcrypt.encode(inputMember.getMemberPw());
//		log.debug("bcryptPassword : " + bcryptPassword);
		
		// bcrypt.matches(평문, 암호화) : 평문과 암호화가 일치하면 true, 아니면 false
		// boolean result = bcrypt.matches(inputMember.getMemberPw(), 
		// '$2a$10$.9m6WzdYXqEIfr01MBCmmuyea9x1jiJvZrBTwqstAO9FxksbdvzeO');
		
		// log.debug("result : " + result );
		
		// 1. 이메일이 일치하면서 탈퇴하지 않은 회원 조회
		Member loginMember = mapper.login(inputMember.getMemberEmail());
		
		// 2. 만약에 일치하는 이메일이 없어서 조회 결과가 null 인 경우
		if(loginMember == null) return null;
		
		// 3. 입력 받은 비밀번호(평문 : inputMember.getMemberPw)와
		//    암호화된 비밀번호(loginMember.getMemberPw())
		//    두 비밀번호가 일치하는지 확인
		
		// 일치하지 않으면
		if( !bcrypt.matches(inputMember.getMemberPw(), loginMember.getMemberPw()) ) {
			return null;
		}
		
		// 로그인 결과에서 비밀번호 제거
		loginMember.setMemberPw(null);

		return loginMember;
	}

	@Override		// 이메일 중복검사 서비스
	public int checkEmail(String memberEmail) {
		return mapper.checkEamil(memberEmail);
	}

	@Override		// 닉네임 중복검사 서비스
	public int checkNickname(String memberNickname) {
		return mapper.checkNickname(memberNickname);
	}

	@Override		// 회원가입 서비스
	public int signup(Member inputMember, String[] memberAddress) {
		
		// 주소가 입력되지 않으면 
		// inputMember.getMemberAddress() -> ",,"
		// memberAddress -> [,,]
		
		if(!inputMember.getMemberAddress().equals(",,")) {	// 주소가 입력된 경우
			// String.join("구분자", 배열) : 배열의 요소 사이에 "구분자" 추가하여 하나의 문자열로 반환
			String address = String.join("^^^", memberAddress);
			
			// 구분자로 "^^^"를 쓴 이유 : 주소, 상세주소에 없는 특수문자 생성
			// -> 나중에 마이페이지에서 주소 수정 시 3분할 때 구분자로 이용할 예정
			
			inputMember.setMemberAddress(address);
			
		} else inputMember.setMemberAddress(null);			// 주소가 입력되지 않은 경우
		
		// 암호화 진행 (현재 inputMember 안의 memberPw 는 평문)
		String encPw = bcrypt.encode(inputMember.getMemberPw());
		inputMember.setMemberPw(encPw);

		return mapper.signup(inputMember);
	}
}
