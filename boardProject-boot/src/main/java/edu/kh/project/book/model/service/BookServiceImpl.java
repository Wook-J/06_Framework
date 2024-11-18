package edu.kh.project.book.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.book.model.dto.Book;
import edu.kh.project.book.model.mapper.BookMapper;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class BookServiceImpl implements BookService{

	private final BookMapper mapper;
	
	@Override
	public List<Book> selectAllList() {
		return mapper.selectAllList();
	}
}
