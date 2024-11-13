package edu.kh.project.email.model.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmailMapper {

	/** 기존 이메일에 대한 인증키 수정
	 * @param map
	 * @return 수정된 행의 개수
	 */
	int updateAuthKey(Map<String, String> map);

	/** 이메일과 인증번호 삽입 (신규)
	 * @param map
	 * @return 삽입된 행의 개수(updateAuthKey 값이 0인 경우 수행)
	 */
	int insertAuthKey(Map<String, String> map);

	/** 입력받은 이메일과 인증 번호가 DB에 있는 지 조회
	 * @param map (email, authKey)
	 * @return
	 */
	int checkAuthKey(Map<String, String> map);


}
