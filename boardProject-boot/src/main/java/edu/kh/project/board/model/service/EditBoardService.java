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

}
