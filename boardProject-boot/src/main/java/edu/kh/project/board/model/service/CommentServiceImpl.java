package edu.kh.project.board.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.board.model.dto.Comment;
import edu.kh.project.board.model.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

	private final CommentMapper mapper;
	
	/* *************** 메서드 *************** */
	
	@Override	// 댓글 목록 조회
	public List<Comment> select(int boardNo) {
		return mapper.select(boardNo);
	}
	
	@Override	// 댓글/답글 등록
	public int insert(Comment comment) {
		return mapper.insert(comment);
	}
	
	@Override	// 댓글/답글 삭제
	public int delete(int commentNo) {
		return mapper.delete(commentNo);
	}
	
	@Override	// 댓글/답글 수정
	public int update(Comment comment) {
		return mapper.update(comment);
	}
}
