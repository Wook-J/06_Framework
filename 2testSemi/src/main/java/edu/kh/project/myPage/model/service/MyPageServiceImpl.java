package edu.kh.project.myPage.model.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.mapper.MyPageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(rollbackFor = Exception.class)
@PropertySource("classpath:/config.properties")
@RequiredArgsConstructor
@Slf4j
public class MyPageServiceImpl implements MyPageService{
	
	private final MyPageMapper mapper;
	private final BCryptPasswordEncoder bcrypt;
	
	@Value("${my.profile.web-path}")
	private String profileWebPath;
	
	@Value("${my.profile.folder-path}")
	private String profileFolderPath;
	
	/* *************** 내 정보 수정 *************** */
	@Override
	public int updateInfo(Member updateMember, String[] memberAddress) {
		
		if(updateMember.getMemberAddress().equals(",,")) updateMember.setMemberAddress(null);
		else {
			String address = String.join("^^^", memberAddress);
			updateMember.setMemberAddress(address);
		}
		
		return mapper.updateInfo(updateMember);
	}
	
	/* *************** 비밀번호 변경 *************** */
	
	
	/* *************** 회원 탈퇴 *************** */

}
