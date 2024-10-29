package edu.kh.demo.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// Lombok 라이브러리 이용
@Getter
@Setter
@ToString			// toString 오버라이딩
@NoArgsConstructor	// 기본 생성자
public class MemberDTO {
	
	// 필드, 멤버변수
	private String memberId;
	private String memberPw;
	private String memberName;
	private int memberAge;

}
