package com.linkin.dao;

import java.util.List;

import com.linkin.entity.CartItem;
import com.linkin.model.CartItemDTO;
import com.linkin.model.SearchCartItemDTO;

public interface CartItemDao {

    void add(CartItem cartItem);

    void edit(CartItem cartItem);

    void delete(Long id);

    CartItem getById(Long id);

    List<CartItem> find(SearchCartItemDTO searchCartItemDTO);

    Long count(SearchCartItemDTO searchCartItemDTO);

    Long countTotal(SearchCartItemDTO searchCartItemDTO);

}
