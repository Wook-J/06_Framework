package edu.kh.project.myPage.model.mapper;

import java.util.Map;

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

	/** 기존 회원의 암호화된 비밀번호 조회
	 * @param memberNo
	 * @return
	 */
	String memberPw(int memberNo);

	/** 기존 회원의 비밀번호 변경
	 * @param member
	 * @return
	 */
	int changePw(Member member);

	/** 기존 회원의 암호화된 비밀번호 조회 (선생님 버전)
	 * @param memberNo
	 * @return
	 */
	String selectPw(int memberNo);

	/** 기존 회원의 비밀번호 변경 (선생님 버전)
	 * @param paramMap
	 * @return
	 */
	int changePwByTeacher(Map<String, Object> paramMap);

	/** 회원 탈퇴
	 * @param memberNo
	 * @return 성공한 행의 개수
	 */
	int secession(int memberNo);

}
