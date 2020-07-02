package com.linkin.model;

public class SearchOrderDTO extends SearchDTO {

	private static final long serialVersionUID = 1L;
	private Integer statusOrder;
	private String productName;
	private String fromDate;
	private String toDate;

	public Integer getStatusOrder() {
		return statusOrder;
	}

	public void setStatusOrder(Integer statusOrder) {
		this.statusOrder = statusOrder;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

}
