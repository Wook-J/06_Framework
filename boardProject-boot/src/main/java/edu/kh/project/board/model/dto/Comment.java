package edu.kh.project.board.model.dto;

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
public class Comment {

	private int commentNo;
	private String commentContent;
	private String commentWriteDate;
	private String commentDelFl;
	private int boardNo;
	private int memberNo;
	private int parentCommentNo;
	
	// 댓글 조회 시 회원 프로필이미지, 닉네임
	private String memberNickname;
	private String profileImg;
}
