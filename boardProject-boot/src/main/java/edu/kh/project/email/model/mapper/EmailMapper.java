package edu.kh.project.email.model.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmailMapper {

	/** 기존 이메일에 대한 인증키 수정
	 * @param map (email, authKey)
	 * @return 수정된 행의 개수(int)
	 */
	int updateAuthKey(Map<String, String> map);

	/** 이메일과 인증번호 삽입(신규)
	 * @param map (email, authKey)
	 * @return 삽입된 행의 개수(int)
	 */
	int insertAuthKey(Map<String, String> map);

	/** 입력받은 이메일과 인증번호가 DB에 있는지 조회
	 * @param map (email, authKey)
	 * @return 1(email 있고 authKey 일치) or 0(그 외)
	 */
	int checkAuthKey(Map<String, String> map);

}
