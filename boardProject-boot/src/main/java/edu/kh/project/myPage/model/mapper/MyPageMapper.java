package edu.kh.project.myPage.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.model.dto.Member;

@Mapper
public interface MyPageMapper {

	/** 회원정보 수정 전 닉네님 중복검사
	 * @param loginMember
	 * @return
	 */
	int updateInfoDuplication(Member loginMember);
	
	/** 회원정보 수정
	 * @param updateMember(update 된 memberNo, memberNickname, memberTel, memberAddress)
	 * @return 업데이트 성공한 행의 개수
	 */
	int updateInfo(Member updateMember);



}
