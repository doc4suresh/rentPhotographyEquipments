package com.kodak.service;

import java.util.List;

import com.kodak.models.Brand;

public interface BrandService {

	List<Brand> findAllActive();
	
	List<Brand> findAll();

	Brand save(Brand brand);

	Brand update(Brand brand);

	void deleteById(long id);
	
	Brand getById(long id);
	
	Brand getByBrandName(String name);
}
