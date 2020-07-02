package com.linkin.service;

import java.util.List;

import com.linkin.model.EmployeeDTO;
import com.linkin.model.SearchEmployeeDTO;

public interface EmployeeService {
	void add(EmployeeDTO employeeDTO);
	
	void update(EmployeeDTO employeeDTO);

	void delete(Long id);
	
	EmployeeDTO getById(Long id);
	
	List<EmployeeDTO> find(SearchEmployeeDTO searchEmployeeDTO);

	Long count(SearchEmployeeDTO searchEmployeeDTO);

	Long countTotal(SearchEmployeeDTO searchEmployeeDTO);

}
