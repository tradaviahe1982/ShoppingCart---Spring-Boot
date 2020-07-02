package com.linkin.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int MAX_10 = 10;
	public static final int MAX_100 = 100;
	public static final int MAX_200 = 200;
	@JsonProperty("search")
	private Search search;
	@JsonProperty("start")
	private Integer start;
	@JsonProperty("length")
	private Integer length;
	@JsonProperty("columns")
	private List<Column> columns;
	@JsonProperty("order")
	private List<Order> orders;

	public SearchDTO() {
		start = 0;
		length = MAX_10;
	}

	public String getKeyword() {
		if (search != null) {
			return search.getValue();
		}
		return null;
	}

	public Search getSearch() {
		return search;
	}

	public void setKeyword(String keyword) {
		Search s = new Search();
		s.setValue(keyword);
		setSearch(s);
	}
	
	public void setSearch(Search search) {
		this.search = search;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLength() {
		if (length != null && length > MAX_200) {
			length = MAX_200;
		}
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public Order getSortBy() {
		if (this.orders != null) {
			Column column = this.columns.get(this.orders.get(0).getColumn());
			this.orders.get(0).setData(column.getData());
			return this.orders.get(0);
		}
		return new Order();
	}

	public static class Order {
		private int column;
		private String dir;
		private String data;

		public int getColumn() {
			return column;
		}

		public void setColumn(int column) {
			this.column = column;
		}

		public String getDir() {
			return dir;
		}

		public void setDir(String dir) {
			this.dir = dir;
		}

		public String getData() {
			return data;
		}

		public void setData(String data) {
			this.data = data;
		}

		public boolean isAsc() {
			return this.dir.equals("asc");
		}
	}
}

class Search {
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}

class Column {
	private String data;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
