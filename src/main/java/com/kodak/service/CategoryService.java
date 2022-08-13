package com.kodak.service;

import java.util.List;

import com.kodak.models.Category;

public interface CategoryService {

	List<Category> findAll();
	
	List<Category> findAllActive();

	Category save(Category brand);

	Category update(Category brand);

	void deleteById(long id);
	
	Category getById(long id);
	
	Category getByCatName(String name);
}
