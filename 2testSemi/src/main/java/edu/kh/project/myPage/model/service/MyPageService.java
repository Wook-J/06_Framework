package edu.kh.project.myPage.model.service;

import edu.kh.project.member.model.dto.Member;

public interface MyPageService {

	/* *************** 내 정보 수정 *************** */
	
	/** 내 정보 업데이트
	 * @param updateMember (프로필 이미지, 닉네임, 전화번호, 주소(아래 가공이용))
	 * @param memberAddress (배열 형태 -> ServiceImpl 에서 데이터 가공)
	 * @return
	 */
	int updateInfo(Member updateMember, String[] memberAddress);
	
	/* *************** 비밀번호 변경 *************** */
	
	
	/* *************** 회원 탈퇴 *************** */

}
