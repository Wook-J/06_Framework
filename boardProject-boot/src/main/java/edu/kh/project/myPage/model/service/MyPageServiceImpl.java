package edu.kh.project.myPage.model.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.mapper.MyPageMapper;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor=Exception.class)		// 모든 예외 발생 시 롤백 or 커밋
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService{

	private final MyPageMapper mapper;
	
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

}
