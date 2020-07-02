package com.linkin.dao;

import java.util.List;

import com.linkin.entity.Supplier;
import com.linkin.model.SearchSupplierDTO;

public interface SupplierDao {
	void add(Supplier supplier);

	void update(Supplier supplier);

	void delete(Long id);

	Supplier getById(Long id);

	List<Supplier> find(SearchSupplierDTO searchSupplierTO);

	Long count(SearchSupplierDTO searchSupplierTO);

	Long countTotal(SearchSupplierDTO searchSupplierTO);
}
