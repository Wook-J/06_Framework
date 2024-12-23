package edu.kh.project.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.model.dto.RefreshTokenDTO;

@Mapper
public interface RefreshTokenMapper {

	List<RefreshTokenDTO> selectRefreshToken(RefreshTokenDTO refreshTokenDTO);

	int selectRefreshTokenCount(RefreshTokenDTO refreshTokenDTO);

	void updateRefreshToken(RefreshTokenDTO refreshTokenDTO);

	void insertRefreshToken(RefreshTokenDTO refreshTokenDTO);

	int updateRefreshTokenInt(RefreshTokenDTO refreshTokenDTO);

	int deleteRefreshTokenInt(String adminRefreshTokenIdx);

}
