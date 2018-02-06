package com.qfox.commons.converter;

import java.util.Date;

import org.junit.Test;

public class TypeConverterTest {

	@Test
	public void test() throws Exception {
		Brand brand = new Brand();
		brand.setId(1L);
		brand.setName("cushow");
		brand.setDateCreated(new Date(1000000000L));

		Shop shop = new Shop();
		shop.setId(2L);
		shop.setName("tianhe");
		shop.setDateCreated(new Date(1000000000L));
		shop.setBrand(brand);

		brand.getShops().add(shop);

		BrandDTO dto = TypeConverter.convert(brand, BrandDTO.class);

		System.out.println(dto);
	}

}
