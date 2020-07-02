package com.linkin.model;

import java.io.Serializable;
import java.util.List;

public class ResponseDTO<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	private long recordsTotal;
	private long recordsFiltered;
	private List<T> data;

	public long getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public long getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(long recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

}
