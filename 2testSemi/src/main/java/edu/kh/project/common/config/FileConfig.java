package edu.kh.project.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.MultipartConfigElement;

@Configuration
@PropertySource("classpath:/config.properties")
public class FileConfig implements WebMvcConfigurer{
	
	@Value("${spring.servlet.multipart.file-size-threshold}")
	private long fileSizeThreshold;				// 파일 업로드 임계값
	
	@Value("${spring.servlet.multipart.max-request-size}")
	private long maxRequestSize;				// HTTP 요청당 파일 최대 크기
	
	@Value("${spring.servlet.multipart.max-file-size}")
	private long maxFileSize;					// 개별 파일당 최대 크기
	
	@Value("${spring.servlet.multipart.location}")
	private String location;					// 임계값 초과 시 파일의 임시 저장 경로
	
	@Value("${my.profile.resource-handler}")
	private String profileResourceHandler;		// 프로필 이미지 관련 경로 1
	
	@Value("${my.profile.resource-location}")
	private String profileResourceLocation;		// 프로필 이미지 관련 경로 2
	
	@Value("${my.board.resource-handler}")
	private String boardResourceHandler;		// 게시글 이미지 관련 경로 1
	
	@Value("${my.board.resource-location}")
	private String boardResourceLocation;		// 게시글 이미지 관련 경로 2
	
	// *************************** 메서드 ***************************
	
	@Override		// 요청주소에 따라 서버컴퓨터의 어떤 경로에 접근할 지 설정
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		registry.addResourceHandler(profileResourceHandler)
		.addResourceLocations(profileResourceLocation);
		
		registry.addResourceHandler(boardResourceHandler)
		.addResourceLocations(boardResourceLocation);
	}
	
	@Bean		// 파일 업로드를 처리하는 사용되는 MultipartConfigElement 반환
	public MultipartConfigElement configElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		
		factory.setFileSizeThreshold(DataSize.ofBytes(fileSizeThreshold));
		factory.setMaxRequestSize(DataSize.ofBytes(maxRequestSize));
		factory.setMaxFileSize(DataSize.ofBytes(maxFileSize));
		factory.setLocation(location);
		
		return factory.createMultipartConfig();
	}
	
	@Bean		// 클라이언트로부터 multipart 요청을 처리하고 MultipartFile 를 제공
	public MultipartResolver multipartResolver () {
		StandardServletMultipartResolver multipartResolver = new StandardServletMultipartResolver();
		return multipartResolver;
	}
	
	

}
