package com.qfox.commons.converter;

import java.util.ArrayList;
import java.util.List;

public class Brand extends Entity<Long> {
	private String name;
	private List<Shop> shops = new ArrayList<Shop>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Shop> getShops() {
		return shops;
	}

	public void setShops(List<Shop> shops) {
		this.shops = shops;
	}

}
