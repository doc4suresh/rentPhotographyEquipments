package com.kodak.controllers;

import java.util.List;

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
import com.kodak.dtos.BranchDto;
import com.kodak.models.Branch;
import com.kodak.models.Employee;
import com.kodak.service.BranchService;
import com.kodak.service.EmployeeService;
import com.kodak.utility.NotificationModel;

@Controller
@RequestMapping("/branch")
public class BranchController {

	private static final Logger LOG = LoggerFactory.getLogger(BranchController.class);

	@Autowired
	private BranchService branchService;

	@Autowired
	private EmployeeService empService;

	private NotificationModel noti;

	@GetMapping()
	public String branch(Model model) {
		List<Branch> branchList = branchService.getAll();
		Branch branch = new Branch();
		List<Employee> managers = empService.getAllManagers();

		model.addAttribute("branch", branch);
		model.addAttribute("branchList", branchList);
		model.addAttribute("managers", managers);

		// If no any notification. assign empty
		NotificationModel noti = new NotificationModel();
		noti.setType(NotiType.NON);
		noti.setMsg("");
		model.addAttribute("noti", this.noti == null ? noti : this.noti);
		this.noti = null;

		return "branch";
	}

	@PostMapping("/addBranch")
	public String addBranch(@ModelAttribute("brnach") BranchDto branchDto, RedirectAttributes redirectAttributes,
			Model model) {
		try {
			List<Branch> existingBranches = branchService.getAll();

			for (Branch row : existingBranches) {
				if (row.getName().equalsIgnoreCase(branchDto.getName())) {
					NotificationModel noti = new NotificationModel();
					noti.setType(NotiType.WARNING);
					noti.setMsg("Branch " + branchDto.getName() + " already exists!");
					this.noti = noti;

					return "redirect:";
				}
			}

			Branch branch = new Branch();
			branch.setName(branchDto.getName());
			branch.setAddress(branchDto.getAddress());
			branch.setPhoneNo(branchDto.getPhoneNo());

			if (branchDto.getManager() != null && !branchDto.getManager().isEmpty()) {
				Employee manager = new Employee();
				List<Employee> managers = empService.getAllManagers();
				for (Employee emp : managers) {
					String name = emp.getFirstName() + " " + emp.getLastName();
					if (branchDto.getManager().equals(name)) {
						manager = emp;
					}
				}
				branch.setManager(manager);
			}

			branchService.save(branch);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.SUCCESS);
			noti.setMsg("Branch " + branchDto.getName() + " saved successfully!");
			this.noti = noti;

			return "redirect:";
		} catch (Exception e) {
			LOG.error("Error occurred while saving the branch", e);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.ERROR);
			noti.setMsg(
					"An error occurred while saving the branch. Please contact the system administrator for further support.");
			this.noti = noti;

			return "redirect:";
		}
	}

	@PostMapping("/editBranch")
	public String editBranch(@ModelAttribute("brnach") BranchDto branchDto) {
		try {
			List<Branch> existingBranches = branchService.getAll();

			for (Branch row : existingBranches) {
				if (row.getName().equalsIgnoreCase(branchDto.getName()) && row.getId() != branchDto.getId()) {
					NotificationModel noti = new NotificationModel();
					noti.setType(NotiType.WARNING);
					noti.setMsg("Branch " + branchDto.getName() + " already exists in ID:" + row.getId());
					this.noti = noti;

					return "redirect:";
				}
			}

			Branch branch = new Branch();
			branch.setId(branchDto.getId());
			branch.setName(branchDto.getName());
			branch.setAddress(branchDto.getAddress());
			branch.setPhoneNo(branchDto.getPhoneNo());

			Employee manager = new Employee();
			List<Employee> managers = empService.getAllManagers();
			for (Employee emp : managers) {
				String name = emp.getFirstName() + " " + emp.getLastName();
				if (branchDto.getManager().equals(name)) {
					manager = emp;
				}
			}
			branch.setManager(manager);

			branchService.update(branch);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.SUCCESS);
			noti.setMsg("Branch " + branch.getName() + " edited successfully!");
			this.noti = noti;

			return "redirect:";
		} catch (Exception e) {
			LOG.error("Error occurred while editing the branch", e);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.ERROR);
			noti.setMsg(
					"An error occurred while editing the branch. Please contact the system administrator for further support.");
			this.noti = noti;

			return "redirect:";
		}
	}

	@GetMapping("/deleteBranch")
	public String deleteBranch(@RequestParam("id") Long id) {
		try {
			branchService.deleteById(id);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.SUCCESS);
			noti.setMsg("Branch deleted successfully!");
			this.noti = noti;

			return "redirect:";
		} catch (Exception e) {
			LOG.error("Error occurred while deleting the branch", e);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.ERROR);
			noti.setMsg(
					"An error occurred while deleting the branch. Please contact the system administrator for further support.");
			this.noti = noti;

			return "redirect:";
		}
	}
}
