package edu.kh.project.member.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.model.dto.Member;

@Mapper
public interface MemberMapper {

	/** 로그인 SQL 실행
	 * @param memberEmail
	 * @return DB에 있는 멤버의 정보
	 */
	Member login(String memberEmail);

	/** 이메일 중복검사 SQL 실행
	 * @param memberEmail
	 * @return
	 */
	int checkEmail(String memberEmail);

}
