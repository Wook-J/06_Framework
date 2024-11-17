package edu.kh.project.common.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@PropertySource("classpath:/config.properties")
public class EmailConfig {

	@Value("${spring.mail.username}")
	private String userName;
	
	@Value("${spring.mail.password}")
	private String password;
	
	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		Properties prop = new Properties();
		
		prop.setProperty("mail.transport.protocol", "smtp");	// 전송 프로토콜을 설정. 여기선 SMTP 사용
		prop.setProperty("mail.smtp.auth", "true"); 			// SMTP 서버 인증을 사용할지 여부를 설정
		prop.setProperty("mail.smtp.starttls.enable", "true");	// STARTTLS를 사용하여 안전한 연결을 활성화할지 여부를 설정
		prop.setProperty("mail.debug", "true"); 				// 디버그 모드를 설정
		prop.setProperty("mail.smtp.ssl.trust", "smtp.gmail.com"); // 신뢰할 수 있는 SMTP 서버 호스트 지정
		prop.setProperty("mail.smtp.ssl.protocols", "TLSv1.2"); // SSL 프로토콜을 설정. 여기선 TLSv1.2 사용
		
		
		mailSender.setUsername(userName);		// 이메일 계정의 사용자 
		mailSender.setPassword(password);		// 이메일 계정의 앱비밀번호
		mailSender.setHost("smtp.gmail.com"); 	// SMTP 서버 호스트를 설정.  Gmail의 SMTP 서버인 "smtp.gmail.com"을 사용
		mailSender.setPort(587); 				// SMTP 서버의 포트 587로 설정
		mailSender.setDefaultEncoding("UTF-8"); // 기본 인코딩을 설정
		mailSender.setJavaMailProperties(prop);	// JavaMail의 속성을 설정(앞서 정의해둔 prop 에 있는 설정들을 추가)
		
		return mailSender;
		
		
	}
}
