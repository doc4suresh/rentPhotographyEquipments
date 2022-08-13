package com.kodak.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodak.models.Category;
import com.kodak.repositories.CategoryRepository;
import com.kodak.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository repo;

	@Override
	public List<Category> findAll() {
		return repo.findAll();
	}

	@Override
	public List<Category> findAllActive() {
		return repo.findAllActive();
	}

	@Override
	public Category save(Category cat) {
		return repo.save(cat);
	}

	@Override
	public Category update(Category cat) {
		return repo.save(cat);
	}

	@Override
	public void deleteById(long id) {
		repo.deleteById(id);
	}

	@Override
	public Category getByCatName(String name) {
		return repo.getByCatName(name);
	}

	@Override
	public Category getById(long id) {
		return repo.getById(id);
	}

}
