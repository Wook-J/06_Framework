package edu.kh.project.email.model.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmailMapper {

	/* *************** 회원가입 *************** */
	int updateAuthKey(Map<String, String> map);		// 기존 이메일 존재 시 인증키 수정
	int insertAuthKey(Map<String, String> map);		// 신규 이메일인 경우 인증키 삽입
	int checkAuthKey(Map<String, String> map);		// 인증번호 확인(비동기)
}
