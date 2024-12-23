package edu.kh.project.myPage.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.dto.UploadFile;

public interface MyPageService {

	
	/** 회원정보 수정 전 닉네님 중복검사 서비스
	 * @param loginMember
	 * @param updateMember
	 * @return
	 */
	int updateInfoDuplication(Member loginMember, Member updateMember);
	
	/** 회원정보 수정 서비스 : 내 버전 (3개 전달해야 해서 별로...??)
	 * @param loginMember : 기존 회원 정보
	 * @param updateMember : 수정할 회원 정보(memberNickname, memberTel, memberAddress)
	 * @param memberAddress : 별도로 수정할 주소 형태
	 * @return
	 */
	int updateInfo(Member loginMember, Member updateMember, String[] memberAddress);

	/** 회원정보 수정 서비스 : 선생님 버전
	 * @param updateMember
	 * @param memberAddress
	 * @return
	 */
	int updateInfo(Member updateMember, String[] memberAddress);

	/** 회원 비밀번호 변경 서비스
	 * @param memberNo
	 * @param currentPw
	 * @param newPw
	 * @return
	 */
	int changePw(int memberNo, String currentPw, String newPw);

	/** 회원 비밀번호 변경 서비스 (선생님 버전)
	 * @param paramMap
	 * @param memberNo
	 * @return
	 */
	int changePwByTeacher(Map<String, Object> paramMap, int memberNo);

	/** 회원 탈퇴 서비스
	 * @param memberPw
	 * @param memberNo
	 * @return result
	 */
	int secession(String memberPw, int memberNo);

	/** 파일 업로드 테스트1
	 * @param uploadFile
	 * @return path
	 */
	String fileUpload1(MultipartFile uploadFile) throws Exception;

	/** 파일 업로드 테스트2 (+DB에 저장)
	 * @param uploadFile
	 * @param memberNo
	 * @return result
	 */
	int fileUpload2(MultipartFile uploadFile, int memberNo) throws Exception;

	/** 파일 목록 조회 서비스
	 * @param memberNo
	 * @return list(UploadFile 형으로 제한)
	 */
	List<UploadFile> fileList(int memberNo);

	/** 여러 파일 업로드 서비스
	 * @param aaaList
	 * @param bbbList
	 * @param memberNo
	 * @return
	 */
	int fileUpload3(List<MultipartFile> aaaList, List<MultipartFile> bbbList, int memberNo) throws Exception;

	/** 프로필 이미지 수정 서비스
	 * @param profileImg
	 * @param loginMember
	 * @return
	 */
	int profile(MultipartFile profileImg, Member loginMember) throws Exception;




}
