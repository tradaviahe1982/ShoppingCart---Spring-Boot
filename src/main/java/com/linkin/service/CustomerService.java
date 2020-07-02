package com.linkin.service;

import java.util.List;

import com.linkin.model.CustomerDTO;
import com.linkin.model.SearchCustomerDTO;

public interface CustomerService {
	void add(CustomerDTO customerDTO);
	
	void update(CustomerDTO customerDTO);

	void delete(Long id);
	
	CustomerDTO getById(Long id);
	
	List<CustomerDTO> find(SearchCustomerDTO searchCustomerDTO);

	Long count(SearchCustomerDTO searchCustomerDTO);

	Long countTotal(SearchCustomerDTO searchCustomerDTO);

}
