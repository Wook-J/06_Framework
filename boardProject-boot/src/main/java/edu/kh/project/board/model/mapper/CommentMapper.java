package edu.kh.project.board.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.board.model.dto.Comment;

@Mapper
public interface CommentMapper {

	/** 댓글 목록 조회
	 * @param boardNo
	 * @return
	 */
	List<Comment> select(int boardNo);
	
	/** 댓글/답글 등록
	 * @param comment (commentContent, boardNo, memberNo, parentCommentNo)
	 * @return
	 */
	int insert(Comment comment);

	/** 댓글/답글 삭제
	 * @param commentNo
	 * @return
	 */
	int delete(int commentNo);

	/** 댓글/답글 수정
	 * @param comment (commentNo, commentContent)
	 * @return
	 */
	int update(Comment comment);


}
