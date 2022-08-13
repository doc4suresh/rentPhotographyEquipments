package com.kodak.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodak.models.Brand;
import com.kodak.repositories.BrandRepository;
import com.kodak.service.BrandService;

@Service
public class BrandServiceImpl implements BrandService {

	@Autowired
	private BrandRepository repo;

	@Override
	public List<Brand> findAll() {
		return repo.findAll();
	}

	@Override
	public List<Brand> findAllActive() {
		return repo.findAllActive();
	}

	@Override
	public Brand save(Brand brand) {
		return repo.save(brand);
	}

	@Override
	public Brand update(Brand brand) {
		return repo.save(brand);
	}

	@Override
	public void deleteById(long id) {
		repo.deleteById(id);
	}

	@Override
	public Brand getByBrandName(String name) {
		return repo.getByBrandName(name);
	}

	@Override
	public Brand getById(long id) {
		return repo.getById(id);
	}

}
