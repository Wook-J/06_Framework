package edu.kh.project.email.model.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import edu.kh.project.email.model.mapper.EmailMapper;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{
	
	private final EmailMapper mapper;
	private final JavaMailSender mailSender;
	private final SpringTemplateEngine templateEngine;
	
	@Override	// 이메일 보내기
	public String sendEmail(String htmlName, String email) {

		String authKey = createAuthKey();	// 인증키 생성
		Map<String, String> map = new HashMap<>();
		map.put("authKey", authKey);
		map.put("email", email);
		
		if(!storeAuthKey(map)) return null;	// DB 저장 시도 실패시 해당 메서드 종료
		
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			helper.setTo(email);
			helper.setSubject("[semiProject] 회원가입 인증번호 입니다");
			helper.setText(loadHtml(authKey, htmlName), true);
			helper.addInline("logo", new ClassPathResource("static/images/logo.jpg"));
			mailSender.send(mimeMessage);
			return authKey;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// sendEmail(String htmlName, String email) 내부 메서드
	private String createAuthKey() {
		return UUID.randomUUID().toString().substring(0, 6);
	}
	
	// sendEmail(String htmlName, String email) 내부 메서드
	@Transactional(rollbackFor=Exception.class)
	private boolean storeAuthKey(Map<String, String> map) {
		int result = mapper.updateAuthKey(map);
		
		if(result == 0) result = mapper.insertAuthKey(map);		// 기존데이터 없는 경우(업데이트 실패)
		
		return result > 0;
	}

	// sendEmail(String htmlName, String email) 내부 메서드
	private String loadHtml(String authKey, String htmlName) {
		Context context = new Context();	// import 시 주의!!! org.thymeleaf.context.Context;
		context.setVariable("authKey", authKey);	// context에 세팅
		
		return templateEngine.process("email/" + htmlName, context);
	}
	
	@Override	// 입력받은 이메일과 인증번호가 DB에 있는 지 조회
	public int checkAuthKey(Map<String, String> map) {
		return mapper.checkAuthKey(map);
	}

}
