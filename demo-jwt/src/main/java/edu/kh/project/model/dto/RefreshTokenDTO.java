package edu.kh.project.model.dto;

import lombok.Data;

@Data
public class RefreshTokenDTO {
	
	private String admRefreshTokenIdx = null;
	private String admIdx = null;
	private String refreshToken = null;
}
