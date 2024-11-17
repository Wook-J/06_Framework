package edu.kh.project.myPage.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.model.dto.Member;

@Mapper
public interface MyPageMapper {

	/* *************** 내 정보 수정 *************** */
	int updateInfo(Member updateMember);
	
	/* *************** 비밀번호 변경 *************** */
	
	
	/* *************** 회원 탈퇴 *************** */
}
