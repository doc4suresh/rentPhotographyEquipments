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
import com.kodak.models.Brand;
import com.kodak.service.BrandService;
import com.kodak.utility.NotificationModel;

@Controller
@RequestMapping("/brand")
public class BrandController {

	private static final Logger LOG = LoggerFactory.getLogger(BrandController.class);

	@Autowired
	private BrandService brandService;

	private NotificationModel noti;

	@GetMapping()
	public String brand(Model model) {

		Brand brand = new Brand();
		List<Brand> brandList = brandService.findAllActive();

		model.addAttribute("brand", brand);
		model.addAttribute("brandList", brandList);

		// If no any notification. assign empty
		NotificationModel noti = new NotificationModel();
		noti.setType(NotiType.NON);
		noti.setMsg("");
		model.addAttribute("noti", this.noti == null ? noti : this.noti);
		this.noti = null;

		return "brand";
	}

	@PostMapping("/addBrand")
	public String addBrand(@ModelAttribute("brand") Brand brand, RedirectAttributes redirectAttributes, Model model) {

		try {
			List<Brand> existBrandList = brandService.findAll();

			for (Brand existbrand : existBrandList) {
				if (existbrand.getBrandName().equalsIgnoreCase(brand.getBrandName())) {
					NotificationModel noti = new NotificationModel();
					noti.setType(NotiType.WARNING);
					noti.setMsg("Item Brand " + brand.getBrandName() + " already exists!");
					this.noti = noti;

					return "redirect:";
				}
			}

			brandService.save(brand);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.SUCCESS);
			noti.setMsg("Item Brand " + brand.getBrandName() + " saved successfully!");
			this.noti = noti;

		} catch (Exception e) {
			LOG.error("Error occurred while saving the Item Brand", e);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.ERROR);
			noti.setMsg(
					"An error occurred while saving the Item Brand. Please contact the system administrator for further support.");
			this.noti = noti;
		}

		return "redirect:";
	}

	@PostMapping("/editBrand")
	public String editBrand(@ModelAttribute("brand") Brand brand, RedirectAttributes redirectAttributes, Model model) {

		try {
			List<Brand> existBrandList = brandService.findAll();

			for (Brand existbrand : existBrandList) {
				if (existbrand.getBrandName().equalsIgnoreCase(brand.getBrandName())
						&& existbrand.getId() != brand.getId()) {
					NotificationModel noti = new NotificationModel();
					noti.setType(NotiType.WARNING);
					noti.setMsg("Item Brand " + brand.getBrandName() + " already existsin ID:" + brand.getId());
					this.noti = noti;

					return "redirect:";
				}
			}

			brandService.update(brand);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.SUCCESS);
			noti.setMsg("Item Brand " + brand.getBrandName() + " edited successfully!");
			this.noti = noti;

		} catch (Exception e) {
			LOG.error("Error occurred while saving the Item Brand", e);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.ERROR);
			noti.setMsg(
					"An error occurred while editing the Item Brand. Please contact the system administrator for further support.");
			this.noti = noti;
		}

		return "redirect:";
	}

	@GetMapping("/deleteBrand")
	public String deleteBrand(@RequestParam("id") Long id) {
		try {

			// Don't delete the brand from the DB. This brand may be used for rent and
			// reports. Hence, just mark the brand as inactive.
			Brand brand = brandService.getById(id);
			brand.setActive(false);
			brandService.save(brand);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.SUCCESS);
			noti.setMsg("Item Brand deleted successfully!");
			this.noti = noti;

			return "redirect:";
		} catch (Exception e) {
			LOG.error("Error occurred while deleting the Item Brand", e);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.ERROR);
			noti.setMsg(
					"An error occurred while deleting the Item Brand. Please contact the system administrator for further support.");
			this.noti = noti;

			return "redirect:";
		}
	}
}
