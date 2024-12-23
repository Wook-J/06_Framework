package edu.kh.project.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.model.dto.AdminDTO;

@Mapper
public interface AdminMapper {

	List<AdminDTO> loadAdminByAdminId(String adminId);

	List<AdminDTO> loadAdminAuthByAdminId(AdminDTO aDTO);

}
