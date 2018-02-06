package com.qfox.commons.converter;

import java.util.ArrayList;
import java.util.List;

public class BrandDTO extends Entity<Long> {
	private String name;
	private List<ShopDTO> shops = new ArrayList<ShopDTO>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ShopDTO> getShops() {
		return shops;
	}

	public void setShops(List<ShopDTO> shops) {
		this.shops = shops;
	}

}
