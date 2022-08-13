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
import com.kodak.models.Category;
import com.kodak.service.CategoryService;
import com.kodak.utility.NotificationModel;

@Controller
@RequestMapping("/category")
public class CategoryController {

	private static final Logger LOG = LoggerFactory.getLogger(CategoryController.class);

	@Autowired
	private CategoryService catService;

	private NotificationModel noti;

	@GetMapping()
	public String category(Model model) {

		Category cat = new Category();
		List<Category> catList = catService.findAllActive();

		model.addAttribute("cat", cat);
		model.addAttribute("catList", catList);

		// If no any notification. assign empty
		NotificationModel noti = new NotificationModel();
		noti.setType(NotiType.NON);
		noti.setMsg("");
		model.addAttribute("noti", this.noti == null ? noti : this.noti);
		this.noti = null;

		return "category";
	}

	@PostMapping("/addCategory")
	public String addCategory(@ModelAttribute("cat") Category cat, RedirectAttributes redirectAttributes, Model model) {

		try {
			List<Category> existCatList = catService.findAll();

			for (Category existCat : existCatList) {
				if (existCat.getCatName().equalsIgnoreCase(cat.getCatName())) {
					NotificationModel noti = new NotificationModel();
					noti.setType(NotiType.WARNING);
					noti.setMsg("Item Category " + cat.getCatName() + " already exists!");
					this.noti = noti;

					return "redirect:";
				}
			}

			catService.save(cat);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.SUCCESS);
			noti.setMsg("Item Category " + cat.getCatName() + " saved successfully!");
			this.noti = noti;

		} catch (Exception e) {
			LOG.error("Error occurred while saving the Item Category", e);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.ERROR);
			noti.setMsg(
					"An error occurred while saving the Item Category. Please contact the system administrator for further support.");
			this.noti = noti;
		}

		return "redirect:";
	}

	@PostMapping("/editCategory")
	public String editCategory(@ModelAttribute("cat") Category cat) {
		try {
			List<Category> existCatList = catService.findAll();

			for (Category existCat : existCatList) {
				if (existCat.getCatName().equalsIgnoreCase(cat.getCatName()) && existCat.getId() != cat.getId()) {
					NotificationModel noti = new NotificationModel();
					noti.setType(NotiType.WARNING);
					noti.setMsg("Item Category " + cat.getCatName() + " already exists in ID:" + existCat.getId());
					this.noti = noti;

					return "redirect:";
				}
			}

			catService.update(cat);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.SUCCESS);
			noti.setMsg("Item Category " + cat.getCatName() + " edited successfully!");
			this.noti = noti;

		} catch (Exception e) {
			LOG.error("Error occurred while editing the Item Category", e);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.ERROR);
			noti.setMsg(
					"An error occurred while editing the Item Category. Please contact the system administrator for further support.");
			this.noti = noti;
		}

		return "redirect:";
	}

	@GetMapping("/deleteCategory")
	public String deleteCategory(@RequestParam("id") Long id) {
		try {
			// Don't delete the category from the DB. This category may be used for rent and
			// reports. Hence, just mark the brand as inactive.
			Category cat = catService.getById(id);
			cat.setActive(false);
			catService.save(cat);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.SUCCESS);
			noti.setMsg("Item Category deleted successfully!");
			this.noti = noti;

			return "redirect:";
		} catch (Exception e) {
			LOG.error("Error occurred while deleting the Item Category", e);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.ERROR);
			noti.setMsg(
					"An error occurred while deleting the Item Category. Please contact the system administrator for further support.");
			this.noti = noti;

			return "redirect:";
		}
	}
}
