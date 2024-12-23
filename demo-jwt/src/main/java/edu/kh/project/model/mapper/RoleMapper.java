package edu.kh.project.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.model.dto.RoleDTO;

@Mapper
public interface RoleMapper {

	List<RoleDTO> selectRole(RoleDTO rDTO);

}
