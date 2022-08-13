package com.kodak.controllers;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kodak.constants.NotiType;
import com.kodak.dtos.EmployeeDto;
import com.kodak.models.Branch;
import com.kodak.models.Employee;
import com.kodak.models.EmployeeRole;
import com.kodak.models.Role;
import com.kodak.service.BranchService;
import com.kodak.service.EmployeeService;
import com.kodak.utility.NotificationModel;
import com.kodak.utility.SecurityUtility;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

	private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

	@Autowired
	private EmployeeService empService;

	@Autowired
	private BranchService branchService;

	private NotificationModel noti;

	@GetMapping()
	public String employee(Model model) {
		List<Employee> empList = empService.findAllActive();
		Employee emp = new Employee();

		List<Branch> branchList = branchService.getAll();

		model.addAttribute("emp", emp);
		model.addAttribute("empList", empList);
		model.addAttribute("branchList", branchList);

		// If no any notification. assign empty
		NotificationModel noti = new NotificationModel();
		noti.setType(NotiType.NON);
		noti.setMsg("");
		model.addAttribute("noti", this.noti == null ? noti : this.noti);
		this.noti = null;

		return "employee";
	}

	@PostMapping("/addEmployee")
	public String addEmployee(@ModelAttribute("emp") EmployeeDto empDto, RedirectAttributes redirectAttributes,
			Model model) {

		try {
			List<Employee> existingEmps = empService.findAll();

			for (Employee row : existingEmps) {
				if (row.getFirstName().equalsIgnoreCase(empDto.getFirstName())
						&& row.getLastName().equalsIgnoreCase(empDto.getLastName())) {
					NotificationModel noti = new NotificationModel();
					noti.setType(NotiType.WARNING);
					noti.setMsg("Employee " + empDto.getFirstName() + " " + empDto.getLastName() + " already exists!");
					this.noti = noti;

					return "redirect:";
				}

				if (row.getUsername().equalsIgnoreCase(empDto.getUsername())) {
					NotificationModel noti = new NotificationModel();
					noti.setType(NotiType.WARNING);
					noti.setMsg(empDto.getUsername()
							+ " username already exists in the system. Please choose another username!");
					this.noti = noti;

					return "redirect:";
				}
			}

			Employee emp = new Employee();
			emp.setFirstName(empDto.getFirstName());
			emp.setLastName(empDto.getLastName());
			emp.setMobile(empDto.getMobile());
			emp.setAddress(empDto.getAddress());
			emp.setDesignation(empDto.getDesignation());
			Branch branch = branchService.getByName(empDto.getBranch());
			emp.setBranch(branch);
			emp.setUsername(empDto.getUsername());
			emp.setPassword(SecurityUtility.passwordEncoder().encode(empDto.getPassword()));
			
			Set<EmployeeRole> userRoles = new HashSet<>();
			Role role1 = new Role();
			role1.setName(empDto.getDesignation());
			userRoles.add(new EmployeeRole(emp, role1));

			empService.createUser(emp, userRoles);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.SUCCESS);
			noti.setMsg("Employee " + empDto.getFirstName() + " " + empDto.getLastName() + " saved successfully!");
			this.noti = noti;

			return "redirect:";

		} catch (Exception e) {
			LOG.error("Error occurred while saving the employee", e);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.ERROR);
			noti.setMsg(
					"An error occurred while saving the employee. Please contact the system administrator for further support.");
			this.noti = noti;

			return "redirect:";
		}

	}

	@PostMapping("/editEmployee")
	public String editEmployee(@ModelAttribute("emp") EmployeeDto empDto) {
		try {
			List<Employee> existingEmp = empService.findAll();

			for (Employee row : existingEmp) {
				if (row.getFirstName().equalsIgnoreCase(empDto.getFirstName())
						&& row.getLastName().equalsIgnoreCase(empDto.getLastName()) && row.getId() != empDto.getId()) {
					NotificationModel noti = new NotificationModel();
					noti.setType(NotiType.WARNING);
					noti.setMsg("Employee " + empDto.getFirstName() + " " + empDto.getLastName()
							+ " already exists in ID:" + row.getId());
					this.noti = noti;

					return "redirect:";
				}

				if (row.getUsername().equalsIgnoreCase(empDto.getUsername()) && row.getId() != empDto.getId()) {
					NotificationModel noti = new NotificationModel();
					noti.setType(NotiType.WARNING);
					noti.setMsg(empDto.getUsername()
							+ " username already exists in the system. Please choose another username!");
					this.noti = noti;

					return "redirect:";
				}
			}

			Employee emp = new Employee();
			emp.setId(empDto.getId());
			emp.setFirstName(empDto.getFirstName());
			emp.setLastName(empDto.getLastName());
			emp.setMobile(empDto.getMobile());
			emp.setAddress(empDto.getAddress());
			emp.setDesignation(empDto.getDesignation());
			Branch branch = branchService.getByName(empDto.getBranch());
			emp.setBranch(branch);
			emp.setUsername(empDto.getUsername());
			if (empDto.getPassword() != null && !empDto.getPassword().isEmpty())
				emp.setPassword(SecurityUtility.passwordEncoder().encode(empDto.getPassword()));

			empService.updateEmployee(emp);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.SUCCESS);
			noti.setMsg("Employee " + emp.getFirstName() + " " + emp.getLastName() + " edited successfully!");
			this.noti = noti;

			return "redirect:";

		} catch (Exception e) {
			LOG.error("Error occurred while editing the employee", e);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.ERROR);
			noti.setMsg(
					"An error occurred while editing the employee. Please contact the system administrator for further support.");
			this.noti = noti;

			return "redirect:";
		}
	}

	@GetMapping("/deleteEmployee")
	public String deleteEmployee(@RequestParam("id") Long id) {
		try {
			// Don't delete the employee. This employee may already used for business
			// process. Hence just inactive the employee.
			Employee emp = empService.getById(id);
			emp.setEnabled(false);
			empService.updateEmployee(emp);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.SUCCESS);
			noti.setMsg("Employee deleted successfully!");
			this.noti = noti;

			return "redirect:";

		} catch (Exception e) {
			LOG.error("Error occurred while deleting the employee", e);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.ERROR);
			noti.setMsg(
					"An error occurred while deleting the employee. Please contact the system administrator for further support.");
			this.noti = noti;

			return "redirect:";
		}
	}

	@PostMapping("/changePassword")
	public String changePassword(@ModelAttribute("oldPassword") String oldPassword,
			@ModelAttribute("newPassword") String newPassword, Principal principal, Model model) {

		try {
			Employee emp = empService.findByUsername(principal.getName());
			String dbEncript = emp.getPassword();
			String oldEncript = SecurityUtility.passwordEncoder().encode(oldPassword);

			NotificationModel noti = new NotificationModel();

			if (dbEncript.equals(oldEncript)) {
				emp.setPassword(SecurityUtility.passwordEncoder().encode(newPassword));
				empService.updateEmployee(emp);

				noti.setType(NotiType.SUCCESS);
				noti.setMsg("Password changed successfully for user " + principal.getName() + "!");
			} else {
				noti.setType(NotiType.ERROR);
				noti.setMsg("Old password is wrong for user " + principal.getName()
						+ ". please try again with correct password!");
			}

			model.addAttribute("noti", this.noti == null ? noti : this.noti);
			this.noti = null;
			return "index";

		} catch (Exception e) {
			LOG.error("Error occurred while saving the employee", e);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.ERROR);
			noti.setMsg(
					"An error occurred while changing the user password. Please contact the system administrator for further support.");
			model.addAttribute("noti", this.noti == null ? noti : this.noti);
			this.noti = null;

			return "index";
		}

	}
}
