package edu.kh.project.member.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.mapper.MemberMapper;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(rollbackFor=Exception.class)
@Slf4j
public class MemberServiceImpl implements MemberService{
	
	@Autowired
	private MemberMapper mapper;
	
	@Autowired		// SecurityConfig.java에서 bean 객체 생성
	public BCryptPasswordEncoder bcrypt;

	@Override		// 로그인 서비스
	public Member login(Member inputMember) {
		
		Member loginMember = mapper.login(inputMember.getMemberEmail());
		
		if(loginMember == null) return null;
		
		if(!bcrypt.matches(inputMember.getMemberPw(), loginMember.getMemberPw())) return null;
		
		loginMember.setMemberPw(null);
		
		return loginMember;
	}

	@Override		// 이메일 중복검사 서비스
	public int checkEmail(String memberEmail) {
		return mapper.checkEmail(memberEmail);
	}

}
