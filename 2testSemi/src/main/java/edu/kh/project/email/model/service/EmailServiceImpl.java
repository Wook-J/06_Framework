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
import jakarta.mail.MessagingException;
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
	
	/* *************** 회원가입 *************** */
	@Override	// 이메일 보내기(비동기)
	public String sendEmail(String htmlName, String email) {
		String authKey = createAuthKey();
		
		Map<String, String> map = new HashMap<>();
		map.put("authKey", authKey);
		map.put("email", email);
		
		if(!storeAuthKey(map)) return null;
		
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		
		try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			helper.setTo(email);
			helper.setSubject("[semiProject] 회원가입 인증번호 입니다.");
			helper.setText(loadHtml(authKey, htmlName), true);
			helper.addInline("logo", new ClassPathResource("static/images/logo.jpg"));
			
			mailSender.send(mimeMessage);
			return authKey;
			
		} catch (MessagingException e) {
			e.printStackTrace();
			return null;
		}
	}

	// sendEmail() 내부 메서드(6자리 인증번호 발급)
	private String createAuthKey() {
		return UUID.randomUUID().toString().substring(0, 6);
	}
	
	// sendEmail() 내부 메서드(인증키와 이메일을 DB에 저장)
	@Transactional(rollbackFor = Exception.class)
	private boolean storeAuthKey(Map<String, String> map) {
		
		int result = mapper.updateAuthKey(map);
		if(result == 0) result = mapper.insertAuthKey(map);
		
		return result > 0;
	}
	
	// sendEmail() 내부 메서드(setText()부분에서 최종 HTML 생성)
	private String loadHtml(String authKey, String htmlName) {
		Context context = new Context();
		context.setVariable("authKey", authKey);
		return templateEngine.process("email/" + htmlName, context);
	}

	@Override	// 인증번호 확인(비동기)
	public int checkAuthKey(Map<String, String> map) {
		return mapper.checkAuthKey(map);
	}
}
