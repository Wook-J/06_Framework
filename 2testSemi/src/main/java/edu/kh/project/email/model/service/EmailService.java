package edu.kh.project.email.model.service;

import java.util.Map;

public interface EmailService {

	/* *************** 회원가입 *************** */
	String sendEmail(String htmlName, String email);	// 이메일 보내기(비동기)
	int checkAuthKey(Map<String, String> map);			// 인증번호 확인(비동기)

}
