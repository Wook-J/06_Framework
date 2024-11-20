package edu.kh.project.board.model.service;

import java.util.List;

import edu.kh.project.board.model.dto.Comment;

public interface CommentService {

	List<Comment> select(int boardNo);		// 댓글 목록 조회
	int insert(Comment comment);			// 댓글/답글 등록
	int delete(int commentNo);				// 댓글/답글 삭제
	int update(Comment comment);			// 댓글/답글 수정

}
