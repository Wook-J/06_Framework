package edu.kh.project.email.model.service;

import java.util.Map;

public interface EmailService {

	/** 이메일 보내기
	 * @param string : 무슨 이메일을 발송할 지 구분할 key
	 * @param email
	 * @return authKey
	 */
	String sendEmail(String htmlName, String email);

	/** 입력받은 이메일과 인증번호가 DB에 있는지 조회
	 * @param map : email, authKey 들어있음
	 * @return
	 */
	int checkAuthKey(Map<String, String> map);

}
