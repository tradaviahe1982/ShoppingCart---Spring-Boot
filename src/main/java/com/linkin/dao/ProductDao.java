package com.linkin.dao;

import java.util.List;

import com.linkin.entity.Product;
import com.linkin.model.SearchProductDTO;

public interface ProductDao {

	void add(Product product);

	void update(Product product);

	void delete(Long id);

	Product getById(Long id);

	List<Product> find(SearchProductDTO searchProductDTO);

	Long count(SearchProductDTO searchProductDTO);

	Long countTotal(SearchProductDTO searchProductDTO);

}
