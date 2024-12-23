package edu.kh.project.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.kh.project.model.dto.RefreshTokenDTO;
import edu.kh.project.model.mapper.RefreshTokenMapper;

@Service
public class RefreshTokenService {
	
	@Autowired RefreshTokenMapper refreshTokenMapper;
	
	public RefreshTokenDTO getRefreshToken(RefreshTokenDTO refreshTokenDTO) throws Exception {
		List<RefreshTokenDTO> list = refreshTokenMapper.selectRefreshToken(refreshTokenDTO);
		
		return list.get(0);
	}

	public void addRefreshToken(RefreshTokenDTO refreshTokenDTO) throws Exception {
		int count = refreshTokenMapper.selectRefreshTokenCount(refreshTokenDTO);
		
		if(count > 0) {		// 이미 토큰이 존재하는 경우 update
			refreshTokenMapper.updateRefreshToken(refreshTokenDTO);
		} else {			// 토큰이 존재하지 않는 경우 insert
			refreshTokenMapper.insertRefreshToken(refreshTokenDTO);
		}
	}
	
	public int editRefreshToken(RefreshTokenDTO refreshTokenDTO) throws Exception {
		return refreshTokenMapper.updateRefreshTokenInt(refreshTokenDTO);
	}
	
	public int delRefreshToken(String adminRefreshTokenIdx) throws Exception {
		return refreshTokenMapper.deleteRefreshTokenInt(adminRefreshTokenIdx);
	}
}
