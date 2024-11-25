package edu.kh.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

// Spring Security(암호화할 때 사용)에서 기본 제공하는 로그인 페이지를 이용하지 않겠다
@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
@EnableScheduling	// 스프링 스케쥴러를 이용하기 위한 활성화 어노테이션
public class BoardProjectBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardProjectBootApplication.class, args);
	}

}
