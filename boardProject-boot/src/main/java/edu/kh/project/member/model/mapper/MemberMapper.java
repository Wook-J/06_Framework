package edu.kh.project.member.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.model.dto.Member;

@Mapper
public interface MemberMapper {
	
	/** 로그인 SQL 실행
	 * @param memberEmail
	 * @return loginMember
	 */
	Member login(String memberEmail) throws Exception;

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

	/** 회원 목록 조회(비동기)
	 * @return
	 */
	List<Member> selectAll();

	/** 특정 회원 비밀번호 초기화(비동기)
	 * @param map(memberNo, encPw)
	 * @return
	 */
	int resetPw(Map<String, Object> map);

	/** 탈퇴한 회원 복구(비동기)
	 * @param memberNo
	 * @return
	 */
	int restoration(int memberNo);

}
