package com.qfox.commons.converter;

public class Shop extends Entity<Long> {
	private String name;
	private Brand brand;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

}
