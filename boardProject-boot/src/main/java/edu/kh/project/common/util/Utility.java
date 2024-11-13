package edu.kh.project.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

// 프로그램 전체적으로 사용할 유용한 기능 모음
public class Utility {

	public static int seqNum = 1;		// 1 ~ 99999 반복
	
	public static String fileRename(String originalFileName) {
		
		// 원하는 형식 : 20241112100105_00003.jpg (오늘날짜+시각_시퀀스번호)
		// SimpleDateFormat : 시간을 원하는 형태의 문자열로 간단히 변경
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		
		// java.util.Date() : 현재 시간을 저장한 자바 객체, cf) java.sql.Date 아님!
		String date = sdf.format(new Date());
		String number = String.format("%05d", seqNum);
		
		seqNum++;
		if(seqNum == 100000) seqNum = 1;
		
		// 확장자 구하기 : substring(), lastIndexOf() 사용
		String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
		
		return date + "_" + number + ext;
	}
}
