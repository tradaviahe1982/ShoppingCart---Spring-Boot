package com.linkin.service;

import java.util.List;

import com.linkin.model.SearchSupplierDTO;
import com.linkin.model.SupplierDTO;

public interface SupplierService {
	void add(SupplierDTO supplierDTO);
	
	void update(SupplierDTO supplierDTO);

	void delete(Long id);
	
	SupplierDTO getById(Long id);
	
	List<SupplierDTO> find(SearchSupplierDTO searchSupplierDTO);

	Long count(SearchSupplierDTO searchSupplierDTO);

	Long countTotal(SearchSupplierDTO searchSupplierDTO);

}
