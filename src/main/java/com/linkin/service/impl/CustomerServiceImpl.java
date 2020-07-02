package com.linkin.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linkin.dao.CustomerDao;
import com.linkin.entity.Customer;
import com.linkin.model.CustomerDTO;
import com.linkin.model.SearchCustomerDTO;
import com.linkin.service.CustomerService;
import com.linkin.utils.DateTimeUtils;
import com.linkin.utils.FileStore;

@Transactional
@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerDao customerDao;

	@Override
	public void add(CustomerDTO customerDTO) {
		Customer customer = new Customer();
		customer.setName(customerDTO.getName());
		customer.setAddress(customerDTO.getAddress());
		customer.setCity(customerDTO.getCity());
		customer.setPhone(customerDTO.getPhone());
		customer.setNote(customerDTO.getNote());
		if (StringUtils.isNotBlank(customerDTO.getBirthDay())) {
			customer.setBirthDay(DateTimeUtils.parseDate(customerDTO.getBirthDay(), DateTimeUtils.DD_MM_YYYY));
		}
		customer.setPathImage(customerDTO.getPathImage());
	//	customer.setStatus(customerDTO.getStatus());
		customerDao.add(customer);
	}

	@Override
	public void update(CustomerDTO customerDTO) {
		Customer customer = customerDao.getById(customerDTO.getId());
		customer.setName(customerDTO.getName());
		customer.setAddress(customerDTO.getAddress());
		customer.setCity(customerDTO.getCity());
		customer.setPhone(customerDTO.getPhone());
		customer.setNote(customerDTO.getNote());
		if (StringUtils.isNotBlank(customerDTO.getBirthDay())) {
			customer.setBirthDay(DateTimeUtils.parseDate(customerDTO.getBirthDay(), DateTimeUtils.DD_MM_YYYY));
		}
		if (customerDTO.getPathImage() != null) {
			final String UPLOAD_FOLDER = FileStore.UPLOAD_FOLDER;
			File avatarFile = new File(UPLOAD_FOLDER + File.separator + customer.getPathImage());

			if (avatarFile.exists()) {
				avatarFile.delete();
			}

			customer.setPathImage(customerDTO.getPathImage());
		}
		//customer.setStatus(customerDTO.getStatus());
		customerDao.update(customer);
	}

	@Override
	public void delete(Long id) {
		Customer Customer = customerDao.getById(id);
		if (Customer != null) {
			customerDao.delete(id);
		}
	}

	@Override
	public CustomerDTO getById(Long id) {
		Customer customer = customerDao.getById(id);
		if (customer != null) {
			CustomerDTO customerDTO = new CustomerDTO();
			customerDTO.setId(customer.getId());
			customerDTO.setName(customer.getName());
			customerDTO.setAddress(customer.getAddress());
			customerDTO.setCity(customer.getCity());
			customerDTO.setNote(customer.getNote());
			customerDTO.setPhone(customer.getPhone());
			if (customer.getBirthDay() != null) {
				customerDTO.setBirthDay(DateTimeUtils.formatDate(customer.getBirthDay(), DateTimeUtils.DD_MM_YYYY));
			}
			customerDTO.setPathImage(customer.getPathImage());
		//	customerDTO.setStatus(customer.getStatus());

			return customerDTO;
		}
		return null;
	}

	@Override
	public List<CustomerDTO> find(SearchCustomerDTO searchCustomerDTO) {
		List<Customer> customers = customerDao.find(searchCustomerDTO);
		List<CustomerDTO> customerDTOs = new ArrayList<CustomerDTO>();
		for (Customer customer : customers) {
			CustomerDTO customerDTO = new CustomerDTO();
			customerDTO.setId(customer.getId());
			customerDTO.setName(customer.getName());
			customerDTO.setAddress(customer.getAddress());
			customerDTO.setCity(customer.getCity());
			customerDTO.setNote(customer.getNote());
			customerDTO.setPhone(customer.getPhone());
			if (customer.getBirthDay() != null) {
				customerDTO.setBirthDay(DateTimeUtils.formatDate(customer.getBirthDay(), DateTimeUtils.DD_MM_YYYY));
			}
			customerDTO.setPathImage(customer.getPathImage());
		//	customerDTO.setStatus(customer.getStatus());
			customerDTOs.add(customerDTO);
		}
		return customerDTOs;
	}

	@Override
	public Long count(SearchCustomerDTO searchCustomerDTO) {
		return customerDao.count(searchCustomerDTO);
	}

	@Override
	public Long countTotal(SearchCustomerDTO searchCustomerDTO) {
		return customerDao.countTotal(searchCustomerDTO);
	}

}
