package edu.kh.project.board.model.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.dto.BoardImg;
import edu.kh.project.board.model.mapper.EditBoardMapper;
import edu.kh.project.common.util.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
@PropertySource("classpath:/config.properties")
@Slf4j
public class EditBoardServiceImpl implements EditBoardService{
	
	private final EditBoardMapper mapper;
	
	@Value("${my.board.web-path}")
	private String webPath;
	
	@Value("${my.board.folder-path}")
	private String folderPath;
	
	/* *************** 메서드 *************** */
	
	@Override
	public int boardInsert(Board inputBoard, List<MultipartFile> images) throws Exception {
		
		// 1. 게시글 부분(inputBoard)을 BOARD 테이블에 INSERT
		// -> INSERT 결과로 작성된 게시글 번호(시퀀스 번호) 반환받기
		int result = mapper.boardInsert(inputBoard);
		// result == INSERT 결과 (삽입 성공한 행의 개수 0 or 1)
		
		// 삽입 실패 시 -> 0 반환
		if(result == 0) return 0;
		
		// 삽입 성공 시 삽입글 게시글의 번호를 변수로 저장
		// -> mapper.xml 에서 <selectKey> 태그를 이용해서 생성된
		//	  boardNo가 inputBoard에 저장된 상태!!! (얕은복사!!!(주소값 복사)/ 깊은복사(주소값 다름))
		// Controller ~ Service ~ Mapper ~ mapper.xml 전부 주소값만 넘기고 있음
		int boardNo = inputBoard.getBoardNo();
		
		// 2. 업로드된 이미지가 실제로 존재할 경우,
		//	  업로드된 이미지만 별도로 저장하여 BOARD_IMG 테이블에 삽입
		
		// 실제 업로드된 이미지의 정보를 모아둘 List 생성
		List<BoardImg> uploadList = new ArrayList<>();
		
		// images 리스트에서 하나씩 꺼내 파일이 있는지 검사
		for(int i=0; i<images.size(); i++) {
			
			if(!images.get(i).isEmpty()) {		// 실제 선택된 파일이 존재하는 경우				
				String originalName = images.get(i).getOriginalFilename();	// 원본명
				String rename = Utility.fileRename(originalName);			// 변경명
				
				BoardImg img = BoardImg.builder()							// DTO 생성
								.imgOriginalName(originalName)
								.imgRename(rename)
								.imgPath(webPath)
								.imgOrder(i)
								.boardNo(boardNo)
								.uploadFile(images.get(i))
								.build();
				
				uploadList.add(img);										// uploadList 에 추가
			}
		}
		
		// 선택한 파일이 전부 없을 경우, 컨트롤러로 현재 제목/상세내용 삽입된 게시글 번호만 리턴
		if(uploadList.isEmpty()) return boardNo;
		
		// 선택한 파일(이미지)이 있을 경우 -> BOARD_IMG 테이블에 INSERT + 서버에 파일 저장
		result = mapper.insertUploadList(uploadList);
		// result == 삽입된 행의 개수 == uploadList.size()
		
		// 다중 INSERT 성공 확인 (uploadList 에 저장된 값이 모두 정상 삽입 되었는가)
		if(result == uploadList.size()) {
			
			for(BoardImg img : uploadList) {
				img.getUploadFile().transferTo(new File(folderPath + img.getImgRename()));
			}
			
		} else {	
			// 부분적으로 삽입 실패
			// ex) uploadList 에 2개 저장했는데 1개만 삽입성공한 경우 -> 전체 서비스 실패로 판단
			//	   이전에 삽입된 내용 모두 rollback
			
			// -> rollback 하는 방법 : RuntimeException 강제 발생 (@Transactional 이 인식)
			throw new RuntimeException();
		}
		
		return boardNo;
	}
	
	@Override	// 게시글 수정
	public int boardUpdate(Board inputBoard, List<MultipartFile> images, String deleteOrderList) throws Exception {
		
		// 1. 게시글 부분(제목/내용) 수정
		int result = mapper.boardUpdate(inputBoard);
		
		// 수정 실패 시 바로 리턴
		if(result == 0) return 0;
		
		// 2. 기존에 있었는데 삭제된 이미지가 있는 경우 (여기 not 연산자 잘보기!!)
		if(deleteOrderList != null && !deleteOrderList.equals("")) {
			Map<String, Object> map = new HashMap<>();
			map.put("deleteOrderList", deleteOrderList);
			map.put("boardNo", inputBoard.getBoardNo());
			
			result = mapper.deleteImage(map);
			
			// 삭제실패한 경우 롤백
			if(result == 0) throw new RuntimeException();
		}
		
		// 3. 선택한 파일이 존재할 경우 해당 파일정보만 모아두는 List 생성
		
		List<BoardImg> uploadList = new ArrayList<>();
		
		// images 리스트에서 하나씩 꺼내 파일이 있는지 검사
		for(int i=0; i<images.size(); i++) {
			
			if(!images.get(i).isEmpty()) {		// 실제 선택된 파일이 존재하는 경우				
				String originalName = images.get(i).getOriginalFilename();	// 원본명
				String rename = Utility.fileRename(originalName);			// 변경명
				
				BoardImg img = BoardImg.builder()							// DTO 생성
						.imgOriginalName(originalName)
						.imgRename(rename)
						.imgPath(webPath)
						.imgOrder(i)
						.boardNo(inputBoard.getBoardNo())
						.uploadFile(images.get(i))
						.build();
				
				uploadList.add(img);										// uploadList 에 추가
				
				// 4. 업로드 하려는 이미지 정보(img)를 이용해서 "수정 또는 삽입" 수행
				// 4-1) 기존에 있었는데 새 이미지로 변경(수정)
				result = mapper.updateImage(img);
				
				// 4-2) 수정 실패(기존 해당순서에 이미지 없는 경우) 시 새 이미지 추가(삽입)
				if(result == 0) result = mapper.insertImage(img);
			}
			
			// 수정 또는 삭제 실패한 경우
			if(result == 0) throw new RuntimeException();
		}
		
		// 선택한 파일이 없을 경우
		if(uploadList.isEmpty()) return result;
		
		// 수정 또는 새 이미지 파일을 서버에 저장
		for(BoardImg img : uploadList) {
			img.getUploadFile().transferTo(new File(folderPath + img.getImgRename()));
		}
		
		return result;
	}
	
}
