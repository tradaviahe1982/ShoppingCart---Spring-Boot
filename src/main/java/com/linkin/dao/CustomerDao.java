package com.linkin.dao;

import java.util.List;

import com.linkin.entity.Customer;
import com.linkin.model.SearchCustomerDTO;

public interface CustomerDao {
	void add(Customer customer);

	void update(Customer customer);

	void delete(Long id);

	Customer getById(Long id);

	List<Customer> find(SearchCustomerDTO searchCustomerDTO);

	Long count(SearchCustomerDTO searchCustomerDTO);

	Long countTotal(SearchCustomerDTO searchCustomerDTO);
}
