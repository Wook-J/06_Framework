package edu.kh.project.member.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.model.dto.Member;

@Mapper
public interface MemberMapper {

	/* *************** 로그인 *************** */
	Member login(String memberEmail);			// 로그인

	/* *************** 회원가입 *************** */
	int checkEmail(String memberEmail);			// 이메일 중복검사(비동기)
	int checkNickname(String memberNickname);	// 닉네임 중복검사(비동기)
	int signup(Member inputMember);				// 회원가입

}
