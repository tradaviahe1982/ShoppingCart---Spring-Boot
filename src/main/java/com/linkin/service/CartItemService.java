package com.linkin.service;

import java.util.List;

import com.linkin.model.CartItemDTO;
import com.linkin.model.SearchCartItemDTO;

public interface CartItemService {
	void add(CartItemDTO cartItemDTO);

	void edit(CartItemDTO cartItemDTO);

	void delete(Long id);

	CartItemDTO getById(Long id);

	List<CartItemDTO> find(SearchCartItemDTO searchCartItemDTO);

	Long count(SearchCartItemDTO searchCartItemDTO);

	Long countTotal(SearchCartItemDTO searchCartItemDTO);
}
