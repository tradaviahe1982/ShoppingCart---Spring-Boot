package com.linkin.model;

import java.io.Serializable;
import java.util.List;

public class OrderDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String note;
	private int statusOrder;
	private String createdDate;
	private String createdByName;
	private Long customerId;
	private String customerName;
	private Long shipperId;
	private String shipperName;
	private Integer discount;
	private Long totalPrice;

	private List<CartItemDTO> cartItemDTOs;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getStatusOrder() {
		return statusOrder;
	}

	public void setStatusOrder(int statusOrder) {
		this.statusOrder = statusOrder;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Long getShipperId() {
		return shipperId;
	}

	public void setShipperId(Long shipperId) {
		this.shipperId = shipperId;
	}

	public String getShipperName() {
		return shipperName;
	}

	public void setShipperName(String shipperName) {
		this.shipperName = shipperName;
	}

	public List<CartItemDTO> getCartItemDTOs() {
		return cartItemDTOs;
	}

	public void setCartItemDTOs(List<CartItemDTO> cartItemDTOs) {
		this.cartItemDTOs = cartItemDTOs;
	}

	public Integer getDiscount() {
		return discount;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
	}

	public Long getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Long totalPrice) {
		this.totalPrice = totalPrice;
	}

}
