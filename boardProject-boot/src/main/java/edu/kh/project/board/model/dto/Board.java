package edu.kh.project.board.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Board {

	// BOARD 테이블 컬럼
	private int boardNo;
	private String boardTitle;
	private String boardContent;
	private String boardWriteDate;
	private String boardUpdateDate;
	private int readCount;
	private String boardDelFl;
	private int boardCode;
	private int memberNo;
	
	// MEMBER 테이블 조인
	private String memberNickname;
	
	// 목록 조회시 상관쿼리 결과
	private int commentCount;
	private int likeCount;
	
	// 게시글 작성자 프로필 이미지 (MEMBER 테이블에 있음)
	private String profileImg;
	
	// 게시글 목록 썸네일 이미지 (BOARD_IMG 테이블에 있음)
	private String thumbnail;
	
	// -- 게시글 상세 조회 시 추가예정 --
	private List<BoardImg> imageList;	// 특정 게시글 이미지 목록
	private List<Comment> commentList;	// 특정 게시글에 작성된 댓글 목록
	private int likeCheck;				// 좋아요 여부 확인
}
