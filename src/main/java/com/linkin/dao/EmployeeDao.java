package com.linkin.dao;

import java.util.List;

import com.linkin.entity.Employee;
import com.linkin.model.SearchEmployeeDTO;

public interface EmployeeDao {
	void add(Employee employee);

	void update(Employee employee);

	void delete(Long id);

	Employee getById(Long id);

	List<Employee> find(SearchEmployeeDTO searchEmployeeDTO);

	Long count(SearchEmployeeDTO searchEmployeeDTO);

	Long countTotal(SearchEmployeeDTO searchEmployeeDTO);
}
