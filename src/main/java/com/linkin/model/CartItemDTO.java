package com.linkin.model;

import java.io.Serializable;

public class CartItemDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private Integer quantity;
	private Long unitPrice;
	private ProductDTO product;
	private Long orderId;

	public CartItemDTO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Long getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Long unitPrice) {
		this.unitPrice = unitPrice;
	}

	public ProductDTO getProduct() {
		return product;
	}

	public void setProduct(ProductDTO product) {
		this.product = product;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}
