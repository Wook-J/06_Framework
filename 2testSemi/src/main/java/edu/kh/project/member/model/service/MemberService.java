package edu.kh.project.member.model.service;

import edu.kh.project.member.model.dto.Member;

public interface MemberService {

	/* *************** 로그인 *************** */
	Member login(Member inputMember);			// 로그인

	/* *************** 회원가입 *************** */
	int checkEmail(String memberEmail);			// 이메일 중복검사(비동기)
	int checkNickname(String memberNickname);	// 닉네임 중복검사(비동기)
	int signup(Member inputMember, String[] memberAddress);	// 회원가입

}
