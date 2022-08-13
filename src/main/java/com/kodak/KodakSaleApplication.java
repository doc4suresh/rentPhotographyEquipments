package com.kodak;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.kodak.models.Employee;
import com.kodak.models.EmployeeRole;
import com.kodak.models.Role;
import com.kodak.models.SpringSecurityAuditorAware;
import com.kodak.service.EmployeeService;
import com.kodak.utility.SecurityUtility;

@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@SpringBootApplication
public class KodakSaleApplication implements CommandLineRunner {

	@Autowired
	private EmployeeService empService;

	@Bean
	public AuditorAware<String> auditorAware() {
		return new SpringSecurityAuditorAware();
	}

	public static void main(String[] args) {
		SpringApplication.run(KodakSaleApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Employee user1 = new Employee();
		user1.setFirstName("Sellaiya");
		user1.setLastName("Suresh");
		user1.setDesignation("Cashier");
		user1.setUsername("suresh");
		user1.setPassword(SecurityUtility.passwordEncoder().encode("123"));
		Set<EmployeeRole> userRoles = new HashSet<>();
		Role role1 = new Role();
		role1.setName("Cashier");
		userRoles.add(new EmployeeRole(user1, role1));

		empService.createUser(user1, userRoles);
	}

}
