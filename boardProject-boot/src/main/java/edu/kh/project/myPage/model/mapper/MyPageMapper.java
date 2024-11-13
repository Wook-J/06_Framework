package edu.kh.project.myPage.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.dto.UploadFile;

@Mapper
public interface MyPageMapper {

	/** 회원정보 수정 전 닉네님 중복검사
	 * @param loginMember
	 * @return
	 */
	int updateInfoDuplication(Member loginMember);
	
	/** 회원정보 수정
	 * @param updateMember(update 된 memberNo, memberNickname, memberTel, memberAddress)
	 * @return 업데이트 성공한 행의 개수
	 */
	int updateInfo(Member updateMember);

	/** 기존 회원의 암호화된 비밀번호 조회
	 * @param memberNo
	 * @return
	 */
	String memberPw(int memberNo);

	/** 기존 회원의 비밀번호 변경
	 * @param member
	 * @return
	 */
	int changePw(Member member);

	/** 기존 회원의 암호화된 비밀번호 조회 (선생님 버전)
	 * @param memberNo
	 * @return
	 */
	String selectPw(int memberNo);

	/** 기존 회원의 비밀번호 변경 (선생님 버전)
	 * @param paramMap
	 * @return
	 */
	int changePwByTeacher(Map<String, Object> paramMap);

	/** 회원 탈퇴
	 * @param memberNo
	 * @return 성공한 행의 개수
	 */
	int secession(int memberNo);

	/** 파일 정보를 DB에 삽입
	 * @param uf
	 * @return result (성공한 행의 개수)
	 */
	int insertUploadFile(UploadFile uf);

	/** 파일 목록 조회
	 * @param memberNo(로그인한 회원)
	 * @return list
	 */
	List<UploadFile> fileList(int memberNo);

	/** 프로필 이미지 변경
	 * @param mem
	 * @return
	 */
	int profile(Member mem);

}
