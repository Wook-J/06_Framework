package edu.kh.project.member.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.model.dto.Member;

@Mapper
public interface MemberMapper {
	
	/** 로그인 SQL 실행
	 * @param memberEmail
	 * @return loginMember
	 */
	Member login(String memberEmail);

	/** 이메일 중복검사
	 * @param memberEmail
	 * @return
	 */
	int checkEamil(String memberEmail);

	/** 닉네임 중복검사
	 * @param memberNickname
	 * @return 중복이면 1 , 아니면 0
	 */
	int checkNickname(String memberNickname);

	/** 회원가입 SQL 실행
	 * @param inputMember
	 * @return result (삽입 성공한 행의 개수)
	 */
	int signup(Member inputMember);

}
