package com.kodak.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodak.models.Branch;
import com.kodak.repositories.BranchRepository;
import com.kodak.service.BranchService;

@Service
public class BranchServiceImpl implements BranchService {

	@Autowired
	private BranchRepository repo;

	@Override
	public List<Branch> getAll() {
		return repo.findAll();
	}

	@Override
	public Branch getById(long id) {
		return repo.getById(id);
	}

	@Override
	public Branch getByName(String name) {
		return repo.getByName(name);
	}

	@Override
	public Branch save(Branch branch) {
		return repo.save(branch);
	}

	@Override
	public Branch update(Branch branch) {
		return repo.save(branch);
	}

	@Override
	public void deleteById(Long id) {
		repo.deleteById(id);
	}

}
