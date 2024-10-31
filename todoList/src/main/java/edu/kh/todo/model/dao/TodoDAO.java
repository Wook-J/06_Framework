package edu.kh.todo.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.todo.model.mapper.TodoMapper;

@Repository		// DAO 계층 역할 명시 + Bean 등록
public class TodoDAO {
	
	@Autowired	// TodoMapper 인터페이스를 상속받은 자식 객체
	private TodoMapper mapper;	// 그 자식 객첵 sqlSessionTemplate 이용

	/** (TEST) todoNo가 1인 할일의 제목 조회
	 * @return title
	 */
	public String testTitle() {
		return mapper.testTitle();
	}

}
