package edu.kh.todo.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.todo.model.dao.TodoDAO;
import edu.kh.todo.model.dto.Todo;
import edu.kh.todo.model.mapper.TodoMapper;

/* @Transactional : 트랜젝션 처리를 수행하라고 지시하는 어노테이션
 * - 정상 코드 수행 시 COMMIT 함
 * - 기본값 : Service 내부 코드 수행 중 RuntimeException 발생 시 rollback
 * 
 * - rollbackFor 속성 : 어떤 예외가 발생했을 때 rollback 할 지 지정
 * */
@Transactional(rollbackFor=Exception.class)
@Service	// 비즈니스 로직(데이터 가공, 트랜젝션 처리) 역할 명시 + Bean 등록
public class TodoServiceImpl implements TodoService{

	@Autowired	// TodoDAO와 같은 타입(OR 상속) Bean 에 DI(의존성주입)
	private TodoDAO dao;
	
	@Autowired	// TodoMapper 인터페이스를 상속받은 자식 객체 DI(의존성주입)
	private TodoMapper mapper;	// 자식 객체가 sqlSessionTemplate를 내부적으로 이용
	
	@Override	// (TEST) todoNo가 1인 할일의 제목 조회
	public String testTitle() {
		return dao.testTitle();
	}
	
	@Override	// 할 일 목록 + 완료된 할 일 개수 조회
	public Map<String, Object> selectAll() {
		// 1. 할 일 목록 조회 (Mapper 호출)
		List<Todo> todoList = mapper.selectAll();
		
		// 2. 완료된 할 일 개수 조회 (Mapper 호출)
		int completeCount = mapper.getCompleteCount();
		
		// 3. 위 2개 결과값을 Map으로 묶어서 반환
		Map<String, Object> map = new HashMap<>();
		map.put("todoList", todoList);
		map.put("completeCount", completeCount);
		return map;
	}

	@Override	// 할 일 추가
	public int addTodo(String todoTitle, String todoContent) {
		// 트랜젝션 제어 처리 -> 클래스 상단에 @Transactional 작성
		
		// (★) MyBatis에서 SQL에 전달할 수 있는 파라미터 개수는 오직 1개!!
		// -> todoTitle, todoContent 를 Todo DTO로 묶어서 전달
		// service 단에서 데이터 가공 처리
		Todo todo = new Todo();
		todo.setTodoTitle(todoTitle);
		todo.setTodoContent(todoContent);
		
		return mapper.addTodo(todo);
	}

	@Override	// 할 일 상세조회
	public Todo todoDetail(int todoNo) {
		return mapper.todoDetail(todoNo);
	}

	@Override	// 완료 여부 변경
	public int changeComplete(Todo todo) {
		return mapper.changeComplete(todo);
	}

	@Override	// 할 일 수정
	public int updateTodo(Todo todo) {
		return mapper.updateTodo(todo);
	}

	@Override	// 할 일 삭제
	public int deleteTodo(int todoNo) {
		return mapper.deleteTodo(todoNo);
	}

	@Override	// 전체 할 일 개수 조회
	public int getTotalCount() {
		return mapper.getTotalCount();
	}

	@Override	// 완료된 할 일 개수 조회
	public int getCompleteCount() {
		return mapper.getCompleteCount();
	}

	@Override	// 할 일 목록 조회
	public List<Todo> selectList() {
		return mapper.selectAll();
	}
}
