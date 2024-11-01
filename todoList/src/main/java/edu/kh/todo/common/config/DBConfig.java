package edu.kh.todo.common.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/*
 * @Configuration
 * - 스프링 설정용 클래스임을 명시 (스프링이 해당 클래스를 설정 정보로 인식하고 사용)
 *   + 객체(Bean)로 생성해서 내부 코드를 서버 실행 시 모두 바로 실행
 * 
 * @PropertySource("경로")
 * - 지정된 경로의 properties 파일 내용을 읽어와 사용
 * - 사용할 properties 파일이 다수일 경우 해당 어노테이션 연속 작성 가능
 * 
 * - classpath: 는 src/main/resources 경로를 의미
 * 
 * @Autowired
 * - 등록된 Bean 중에서 타입이 일치하거나 상속관계게 있는 Bean을
 *   지정된 필드에 주입 ( == 의존성 주입)
 *   
 * @ConfigurationProperties(prefix = "spring.datasource.hikari")
 * - @PropertySource 로 읽어온 config.properties 파일의 내용 중 
 *   접두사(앞부분, prefix)가 일치하는 값만 읽어옴
 *   
 * @Bean
 * - 개발자가 수동으로 생성한 객체의 관리를 스프링에게 넘기는 어노테이션
 * - @Bean 어노테이션이 작성된 메서드에서 반환된 객체는 Spring Container가 관리(IOC)
 * */
@Configuration
@PropertySource("classpath:/config.properties")
public class DBConfig {
	
	// 필드, 멤버변수
	@Autowired		// 의존성 주입(DI)과 관련된 어노테이션
	private ApplicationContext applicationContext;		
	// application scope 객체 : 즉, 현재 프로젝트
	// -> 스프링이 관리하고 있는 ApplicationContext 객체를 의존성 주입 받음
	// -> 현재 프로젝트의 전반적인 설정과 Bean 관리에 접근할 수 있도록 해줌
	// (주의) import org.springframework.context.ApplicationContext; (다른 클래스도 있음)
	
	
	// 매서드
	
	/////////////// HikariCP(Connection Pool) 설정부분 ///////////////
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	public HikariConfig hikariConfig() {
		
		// -> config.properties 파일에서 읽어온 spring.datasource.hikari로
		//    시작하는 모든 값이 자동으로 알맞은 필드에 세팅됨
		return new HikariConfig();
	}
	
	@Bean
	public DataSource dataSource(HikariConfig config) {
		// 매개변수 HikariConfig config
		// -> 등록된 Bean 중 HikariConfig 타입의 Bean을 자동으로 주입
		// -> HikariConfig 객체를 받아 설정된 HikariConfig를 통해 DataSource객체 생성
		DataSource dataSource = new HikariDataSource(config);
		
		// DataSource : 애플리케이션이 DB에 연결할 때 사용하는 설정
		// 1) DB 연결정보를 제공 (url, username, password)
		// 2) Connection Pool 관리
		// 3) 트랜젝션 관리
		return dataSource;
	}
	
	// mapper 파일 2개 만들고 다시 돌아오기
	
	/////////////// Mybatis 설정부분 ///////////////
	// Mybatis : Java 애플리케이션에서 SQL을 더 쉽게 사용할 수 있도록 도와주는 영속성 프레임워크
	// 영속성 프레임워크(Persistence Framework) : 애플리케이션의 데이터를 DB와 같은 저장소에
	//    영구적으로 저장하고, 이를 쉽게 조회, 수정, 삭제 등 할 수 있도록 도와주는 프레임워크
	
	// SqlSessionFactory : SqlSession을 만드는 객체
	@Bean					// 매개변수로 DataSource를 받아와 DB 연결정보를 사용할 수 있도록 함
	public SqlSessionFactory sessionFactory(DataSource dataSource) throws Exception{
		
		// MyBatis의 SQL 세션을 생성하는 역할을 할 객체 생성
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		
		sessionFactoryBean.setDataSource(dataSource);
		
		// *** sessionFactoryBean이라는 공장에 MyBatis를 이용하기 위한 각종 세팅중..! ***
		
		// 세팅 1. mapper.xml(SQL 작성해둘 파일) 파일이 모여있는 경로 지정
		// -> MyBatis 코드 수행 시 mapper.xml 을 읽을 수 있음		src/main/resources/mappers 하위의 모든 .xml 파일
		sessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:/mappers/**.xml"));
											// 현재프로젝트의.자원을 얻어옴 

		// 세팅 2. 해당 패키지 내 모든 클래스의 별칭 등록
		// - MyBatis는 특정 클래스 지정 시 패키지명.클래스명을 모두 작성해야함..!
		// -> 긴 이름을 짧게 부를 수 있도록 별칭 설정할 수 있음
		
		// 별칭을 지정해야하는 DTO가 모여있는 패키지 지정
		// -> 해당 패키지에 있는 모든 클래스가 클래스명으로 별칭이 지정됨
		// 예시코드 : sessionFactoryBean.setTypeAliasesPackage("edu.kh.project.member.model.dto");
		
		// setTypeAliasesPackage("패키지명") 이용 시 패키지 내 클래스 파일명이 별칭으로 등록됨
		// ex) 원본 edu.kh.todo.model.dto.Todo --> Todo (별칭 등록)
		sessionFactoryBean.setTypeAliasesPackage("edu.kh.todo");
		
		// 세팅 3. 마이바티스 설정 파일 경로 지정					src/main/resources/mybatis-config.xml
		sessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:/mybatis-config.xml"));
											// 현재프로젝트의 자원을 얻어옴
		// SqlSession 객체 반환
		return sessionFactoryBean.getObject();
	}

	// SqlSessionTemplate : 기본 SQL 실행 + 트랜잭션 처리
	// Connection + DBCP + MyBatis + 트랜젝션 처리
	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sessionFactory) {
		return new SqlSessionTemplate(sessionFactory);
	}
	
	// DataSourceTransactionManager : 트랜잭션 매니저
	@Bean
	public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

}
