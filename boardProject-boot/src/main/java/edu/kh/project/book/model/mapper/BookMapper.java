package edu.kh.project.book.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.book.model.dto.Book;

@Mapper
public interface BookMapper {

	List<Book> selectAllList();

}
