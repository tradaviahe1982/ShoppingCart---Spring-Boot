package com.linkin.dao;

import java.util.List;

import com.linkin.entity.Order;
import com.linkin.model.SearchOrderDTO;

public interface OrderDao {
	void add(Order order);

	void update(Order order);

	void delete(Long id);

	Order getById(Long id);

	List<Order> find(SearchOrderDTO searchOrderDTO);

	Long count(SearchOrderDTO searchOrderDTO);

	Long countTotal(SearchOrderDTO searchOrderDTO);
}
