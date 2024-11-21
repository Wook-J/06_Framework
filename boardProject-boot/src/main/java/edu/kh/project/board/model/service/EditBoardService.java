package edu.kh.project.board.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.board.model.dto.Board;

public interface EditBoardService {

	/** 게시글 작성
	 * @param inputBoard
	 * @param images
	 * @return
	 * @throws Exception (transferTo 처리)
	 */
	int boardInsert(Board inputBoard, List<MultipartFile> images) throws Exception;

	/** 게시글 수정
	 * @param inputBoard (boardNo, boardTitle, boardContent, boardCode, memberNo)
	 * @param images
	 * @param deleteOrderList
	 * @return
	 */
	int boardUpdate(Board inputBoard, List<MultipartFile> images, String deleteOrderList) throws Exception;

}
