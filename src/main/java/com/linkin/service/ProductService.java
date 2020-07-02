package com.linkin.service;

import java.util.List;

import com.linkin.model.ProductDTO;
import com.linkin.model.SearchProductDTO;

public interface ProductService {
	void add(ProductDTO productDTO);

	void update(ProductDTO productDTO);

	void delete(Long id);

	ProductDTO getById(Long id);

	List<ProductDTO> find(SearchProductDTO searchProductDTO);

	Long count(SearchProductDTO searchProductDTO);

	Long countTotal(SearchProductDTO searchProductDTO);
}
