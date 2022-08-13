package com.kodak.service;

import java.util.List;

import com.kodak.models.Branch;

public interface BranchService {

	List<Branch> getAll();

	Branch getById(long id);

	Branch getByName(String name);

	Branch save(Branch branch);

	Branch update(Branch branch);

	void deleteById(Long id);
}
