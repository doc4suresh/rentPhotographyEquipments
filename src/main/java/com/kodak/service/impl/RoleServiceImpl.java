package com.kodak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodak.models.Role;
import com.kodak.repositories.RoleRepository;
import com.kodak.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepo;

	@Override
	public Role save(Role role) {
		return roleRepo.save(role);
	}

}
