package com.linkin.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linkin.dao.CartItemDao;
import com.linkin.dao.OrderDao;
import com.linkin.dao.ProductDao;
import com.linkin.entity.CartItem;
import com.linkin.entity.Customer;
import com.linkin.entity.Order;
import com.linkin.entity.Product;
import com.linkin.entity.Shipper;
import com.linkin.model.CartItemDTO;
import com.linkin.model.OrderDTO;
import com.linkin.model.ProductDTO;
import com.linkin.model.SearchOrderDTO;
import com.linkin.service.OrderService;
import com.linkin.utils.DateTimeUtils;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	OrderDao orderDao;
	@Autowired
	CartItemDao cartItemDao;
	@Autowired
	ProductDao productDao;

	@Override
	public void add(OrderDTO orderDTO) {
		Order order = new Order();
		order.setNote(orderDTO.getNote());
		order.setStatusOrder(orderDTO.getStatusOrder());
		order.setDiscount(orderDTO.getDiscount());

		if (orderDTO.getShipperId() != null) {
			order.setShipper(new Shipper(orderDTO.getShipperId()));
		}

		if (orderDTO.getCustomerId() != null) {
			order.setCustomer(new Customer(orderDTO.getCustomerId()));
		}
		
		if (orderDTO.getCartItemDTOs() != null) {
			List<CartItem> cartItemList = new ArrayList<>();
			long totalPrice = 0;
			for (CartItemDTO cartItemDTO : orderDTO.getCartItemDTOs()) {
				CartItem cartItem = new CartItem();
				cartItem.setOrder(order);
				cartItem.setQuantity(cartItemDTO.getQuantity());
				cartItem.setUnitPrice(cartItemDTO.getUnitPrice());
				cartItem.setProduct(new Product(cartItemDTO.getProduct().getId()));
				cartItemList.add(cartItem);
				totalPrice += cartItemDTO.getQuantity() + cartItemDTO.getUnitPrice();
			}
			
			order.setCartItemList(cartItemList);
			order.setTotalPrice(totalPrice);
		}
		orderDao.add(order);
	}

	@Override
	public void update(OrderDTO orderDTO) {
		Order order = orderDao.getById(orderDTO.getId());
		if (orderDTO != null) {
			order.setNote(orderDTO.getNote());
			order.setStatusOrder(orderDTO.getStatusOrder());

			orderDao.update(order);
		}
	}

	@Override
	public void delete(Long id) {
		Order order = orderDao.getById(id);
		if (order != null) {
			orderDao.delete(id);
		}
	}

	@Override
	public OrderDTO getById(Long id) {
		Order order = orderDao.getById(id);
		if (order != null) {
			OrderDTO orderDTO = new OrderDTO();
			orderDTO.setId(order.getId());
			orderDTO.setNote(order.getNote());
			orderDTO.setDiscount(order.getDiscount());
			orderDTO.setTotalPrice(order.getTotalPrice());
			orderDTO.setCreatedDate(
					DateTimeUtils.formatDate(order.getCreatedDate(), DateTimeUtils.DD_MM_YYYY_HH_MM_SS));
			orderDTO.setCreatedByName(order.getCreatedBy().getName());
			orderDTO.setStatusOrder(order.getStatusOrder());
			if (order.getCustomer() != null) {
				orderDTO.setCustomerId(order.getCustomer().getId());
				orderDTO.setCustomerName(order.getCustomer().getName());
			}
			if (order.getShipper() != null) {
				orderDTO.setShipperId(order.getShipper().getId());
				orderDTO.setShipperName(order.getShipper().getName());
			}
			List<CartItemDTO> cartItemDTOs = new ArrayList<>();
			if (order.getCartItemList() != null) {
				for (CartItem cartItem : order.getCartItemList()) {
					CartItemDTO cartItemDTO = new CartItemDTO();
					cartItemDTO.setId(cartItem.getId());
					cartItemDTO.setQuantity(cartItem.getQuantity());
					cartItemDTO.setUnitPrice(cartItem.getUnitPrice());

					ProductDTO productDTO = new ProductDTO();
					productDTO.setId(cartItem.getProduct().getId());
					productDTO.setName(cartItem.getProduct().getName());
					productDTO.setPathImage(cartItem.getProduct().getPathImage());
					cartItemDTO.setProduct(productDTO);

					cartItemDTOs.add(cartItemDTO);
				}
				orderDTO.setCartItemDTOs(cartItemDTOs);
			}
			return orderDTO;
		}
		return null;
	}

	@Override
	public Long count(SearchOrderDTO searchOrderDTO) {
		return orderDao.count(searchOrderDTO);
	}

	@Override
	public Long countTotal(SearchOrderDTO searchOrderDTO) {
		return orderDao.countTotal(searchOrderDTO);
	}

	@Override
	public List<OrderDTO> find(SearchOrderDTO searchOrderDTO) {
		List<Order> orders = orderDao.find(searchOrderDTO);
		List<OrderDTO> orderDTOs = new ArrayList<OrderDTO>();
		for (Order order : orders) {
			OrderDTO orderDTO = new OrderDTO();
			orderDTO.setId(order.getId());
			orderDTO.setNote(order.getNote());
			orderDTO.setDiscount(order.getDiscount());
			orderDTO.setTotalPrice(order.getTotalPrice());
			orderDTO.setCreatedDate(
					DateTimeUtils.formatDate(order.getCreatedDate(), DateTimeUtils.DD_MM_YYYY_HH_MM_SS));
			orderDTO.setCreatedByName(order.getCreatedBy().getName());
			orderDTO.setStatusOrder(order.getStatusOrder());
			if (order.getCustomer() != null) {
				orderDTO.setCustomerId(order.getCustomer().getId());
				orderDTO.setCustomerName(order.getCustomer().getName());
			}
			if (order.getShipper() != null) {
				orderDTO.setShipperId(order.getShipper().getId());
				orderDTO.setShipperName(order.getShipper().getName());
			}
			List<CartItemDTO> cartItemDTOs = new ArrayList<>();
			if (order.getCartItemList() != null) {
				for (CartItem cartItem : order.getCartItemList()) {
					CartItemDTO cartItemDTO = new CartItemDTO();
					cartItemDTO.setId(cartItem.getId());
					cartItemDTO.setQuantity(cartItem.getQuantity());
					cartItemDTO.setUnitPrice(cartItem.getUnitPrice());

					ProductDTO productDTO = new ProductDTO();
					productDTO.setId(cartItem.getProduct().getId());
					productDTO.setName(cartItem.getProduct().getName());
					productDTO.setPathImage(cartItem.getProduct().getPathImage());
					cartItemDTO.setProduct(productDTO);

					cartItemDTOs.add(cartItemDTO);
				}
				orderDTO.setCartItemDTOs(cartItemDTOs);
			}

			orderDTOs.add(orderDTO);
		}

		return orderDTOs;
	}

}
