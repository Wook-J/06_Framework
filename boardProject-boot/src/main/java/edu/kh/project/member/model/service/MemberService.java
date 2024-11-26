package edu.kh.project.member.model.service;

import java.util.List;

import edu.kh.project.member.model.dto.Member;

public interface MemberService {

	/** 로그인 서비스
	 * @param inputMember
	 * @return loginMember
	 */
	Member login(Member inputMember) throws Exception;

	/** 이메일 중복검사 서비스
	 * @param memberEmail
	 * @return
	 * @author 누가만들었는지 적어놓기!
	 */
	int checkEmail(String memberEmail);

	/** 닉네임 중복검사 서비스
	 * @param inputNickname
	 * @return 중복이면 1 , 아니면 0
	 */
	int checkNickname(String memberNickname);

	/** 회원가입 서비스
	 * @param inputMember
	 * @param memberAddress
	 * @return result
	 */
	int signup(Member inputMember, String[] memberAddress);

	/** 회원 조회 서비스(비동기)
	 * @return
	 */
	List<Member> selectList();

	/** 특정 회원 비밀번호 초기화 서비스(비동기)
	 * @param memberNo
	 * @return 비밀번호 초기화 성공한 행의 개수
	 */
	int resetPw(int memberNo);

	/** 탈퇴한 회원 복구 서비스(비동기)
	 * @param memberNo
	 * @return 복구 성공한 행의 개수
	 */
	int restoration(int memberNo);

}
