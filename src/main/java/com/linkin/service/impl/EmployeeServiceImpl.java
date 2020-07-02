package com.linkin.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linkin.dao.EmployeeDao;
import com.linkin.dao.UserDao;
import com.linkin.entity.Employee;
import com.linkin.entity.Role;
import com.linkin.entity.User;
import com.linkin.model.EmployeeDTO;
import com.linkin.model.SearchEmployeeDTO;
import com.linkin.service.EmployeeService;
import com.linkin.utils.DateTimeUtils;
import com.linkin.utils.FileStore;
import com.linkin.utils.PasswordGenerator;
import com.linkin.utils.RoleEnum;

@Transactional
@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	private UserDao userDao;

	@Override
	public void add(EmployeeDTO employeeDTO) {
		Employee employee = new Employee();
		employee.setName(employeeDTO.getName());
		employee.setAddress(employeeDTO.getAddress());
		employee.setCity(employeeDTO.getCity());
		employee.setPhone(employeeDTO.getPhone());
		employee.setNote(employeeDTO.getNote());
		if (StringUtils.isNotBlank(employeeDTO.getBirthDay())) {
			employee.setBirthDay(DateTimeUtils.parseDate(employeeDTO.getBirthDay(), DateTimeUtils.DD_MM_YYYY));
		}
		employee.setPathImage(employeeDTO.getPathImage());

		User user = new User();
		user.setEmployee(employee);
		user.setName(employeeDTO.getName());
		user.setPhone(employeeDTO.getUsername());
		user.setPassword(PasswordGenerator.getHashString(employeeDTO.getPassword()));
		user.setEnabled(true);
		user.setRole(new Role((long) RoleEnum.STAFF.getId()));

		userDao.add(user);
		employeeDao.add(employee);
	}

	@Override
	public void update(EmployeeDTO employeeDTO) {
		Employee employee = employeeDao.getById(employeeDTO.getId());
		employee.setName(employeeDTO.getName());
		employee.setAddress(employeeDTO.getAddress());
		employee.setCity(employeeDTO.getCity());
		employee.setPhone(employeeDTO.getPhone());
		employee.setNote(employeeDTO.getNote());
		if (StringUtils.isNotBlank(employeeDTO.getBirthDay())) {
			employee.setBirthDay(DateTimeUtils.parseDate(employeeDTO.getBirthDay(), DateTimeUtils.DD_MM_YYYY));
		}
		if (employeeDTO.getPathImage() != null) {
			final String UPLOAD_FOLDER = FileStore.UPLOAD_FOLDER;
			File avatarFile = new File(UPLOAD_FOLDER + File.separator + employee.getPathImage());

			if (avatarFile.exists()) {
				avatarFile.delete();
			}

			employee.setPathImage(employeeDTO.getPathImage());
		}
		User user = employee.getUser();
		if (user != null) {
			user.setName(employee.getName());
		}
		employeeDao.update(employee);
	}

	@Override
	public void delete(Long id) {
		Employee Employee = employeeDao.getById(id);
		if (Employee != null) {
			employeeDao.delete(id);
		}
	}

	@Override
	public EmployeeDTO getById(Long id) {
		Employee employee = employeeDao.getById(id);
		if (employee != null) {
			EmployeeDTO employeeDTO = new EmployeeDTO();
			employeeDTO.setId(employee.getId());
			employeeDTO.setName(employee.getName());
			employeeDTO.setAddress(employee.getAddress());
			employeeDTO.setCity(employee.getCity());
			employeeDTO.setNote(employee.getNote());
			employeeDTO.setPhone(employee.getPhone());
			if (employee.getBirthDay() != null) {
				employeeDTO.setBirthDay(DateTimeUtils.formatDate(employee.getBirthDay(), DateTimeUtils.DD_MM_YYYY));
			}
			employeeDTO.setPathImage(employee.getPathImage());

			return employeeDTO;
		}
		return null;
	}

	@Override
	public List<EmployeeDTO> find(SearchEmployeeDTO searchEmployeeDTO) {
		List<Employee> employees = employeeDao.find(searchEmployeeDTO);
		List<EmployeeDTO> employeeDTOs = new ArrayList<EmployeeDTO>();
		for (Employee employee : employees) {
			EmployeeDTO employeeDTO = new EmployeeDTO();
			employeeDTO.setId(employee.getId());
			employeeDTO.setName(employee.getName());
			employeeDTO.setAddress(employee.getAddress());
			employeeDTO.setCity(employee.getCity());
			employeeDTO.setNote(employee.getNote());
			employeeDTO.setPhone(employee.getPhone());
			if (employee.getBirthDay() != null) {
				employeeDTO.setBirthDay(DateTimeUtils.formatDate(employee.getBirthDay(), DateTimeUtils.DD_MM_YYYY));
			}
			employeeDTO.setPathImage(employee.getPathImage());
			employeeDTOs.add(employeeDTO);
		}
		return employeeDTOs;
	}

	@Override
	public Long count(SearchEmployeeDTO searchEmployeeDTO) {
		return employeeDao.count(searchEmployeeDTO);
	}

	@Override
	public Long countTotal(SearchEmployeeDTO searchEmployeeDTO) {
		return employeeDao.countTotal(searchEmployeeDTO);
	}

}
