package com.linkin.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.linkin.dao.CartItemDao;
import com.linkin.entity.CartItem;
import com.linkin.entity.Order;
import com.linkin.entity.Product;
import com.linkin.model.CartItemDTO;
import com.linkin.model.ProductDTO;
import com.linkin.model.SearchCartItemDTO;
import com.linkin.service.CartItemService;

@Service
@Transactional
public class CartItemServiceImpl implements CartItemService {

	@Autowired
	private CartItemDao cartItemDao;

	// lấy xuống databse của thằng product, cái thuộc tính productId của thằng
	// cartItem lấy đc thằng product,và lấy được đơn hàng,thuộc về đơn hàng nào
	@Override
	public void add(CartItemDTO cartItemDTO) {
		CartItem cartItem = new CartItem();
		cartItem.setQuantity(cartItemDTO.getQuantity());
		cartItem.setUnitPrice(cartItemDTO.getUnitPrice());
		cartItem.setProduct(new Product(cartItemDTO.getProduct().getId()));
		cartItem.setOrder(new Order(cartItemDTO.getOrderId()));
		
		cartItemDao.add(cartItem);
	}

	@Override
	public void edit(CartItemDTO cartItemDTO) {
		CartItem cartItem = cartItemDao.getById(cartItemDTO.getId());
		if (cartItem != null) {
			cartItem.setQuantity(cartItemDTO.getQuantity());
			cartItemDao.edit(cartItem);
		}
	}

	@Override
	public void delete(Long id) {
		CartItem cartItem = cartItemDao.getById(id);
		if (cartItem != null) {
			cartItemDao.delete(id);
		}
	}

	@Override
	public CartItemDTO getById(Long id) {
		CartItem cartItem = cartItemDao.getById(id);
		if (cartItem != null) {
			CartItemDTO cartItemDTO = new CartItemDTO();
			cartItemDTO.setQuantity(cartItem.getQuantity());
			cartItemDTO.setUnitPrice(cartItem.getUnitPrice());

			ProductDTO productDTO = new ProductDTO();
			productDTO.setId(cartItem.getProduct().getId());
			productDTO.setName(cartItem.getProduct().getName());
			productDTO.setPathImage(cartItem.getProduct().getPathImage());
			cartItemDTO.setProduct(productDTO);

			return cartItemDTO;
		}
		return null;
	}

	@Override
	public List<CartItemDTO> find(SearchCartItemDTO searchCartItemDTO) {
		List<CartItem> cartItems = cartItemDao.find(searchCartItemDTO);
		List<CartItemDTO> cartItemDTOs = new ArrayList<CartItemDTO>();
		for (CartItem cartItem : cartItems) {
			CartItemDTO cartItemDTO = new CartItemDTO();
			cartItemDTO.setQuantity(cartItem.getQuantity());
			cartItemDTO.setUnitPrice(cartItem.getUnitPrice());

			ProductDTO productDTO = new ProductDTO();
			productDTO.setId(cartItem.getProduct().getId());
			productDTO.setName(cartItem.getProduct().getName());
			productDTO.setPathImage(cartItem.getProduct().getPathImage());
			cartItemDTO.setProduct(productDTO);
			
			cartItemDTOs.add(cartItemDTO);
		}
		return cartItemDTOs;
	}

	@Override
	public Long count(SearchCartItemDTO searchCartItemDTO) {
		return cartItemDao.count(searchCartItemDTO);
	}

	@Override
	public Long countTotal(SearchCartItemDTO searchCartItemDTO) {
		return cartItemDao.countTotal(searchCartItemDTO);
	}

}
