package edu.kh.project.book.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Book {
	private int bookNo;
	private String title;		// 바꿨음
	private String writer;		// 바꿨음
	private int price;			// 바꿨음
}
