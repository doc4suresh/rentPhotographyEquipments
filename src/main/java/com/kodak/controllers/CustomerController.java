package com.kodak.controllers;

import java.io.FileOutputStream;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kodak.constants.NotiType;
import com.kodak.dtos.CustomerDto;
import com.kodak.models.Customer;
import com.kodak.service.CustomerService;
import com.kodak.utility.FilePath;
import com.kodak.utility.NotificationModel;

@Controller
@RequestMapping("/customer")
public class CustomerController {

	private static final Logger LOG = LoggerFactory.getLogger(CustomerController.class);

	@Autowired
	private CustomerService cusService;

	private NotificationModel noti;

	@GetMapping()
	public String customer(Model model) {
		List<Customer> cusList = cusService.findAllActive();
		CustomerDto cus = new CustomerDto();

		model.addAttribute("cus", cus);
		model.addAttribute("cusList", cusList);

		// If no any notification. assign empty
		NotificationModel noti = new NotificationModel();
		noti.setType(NotiType.NON);
		noti.setMsg("");
		model.addAttribute("noti", this.noti == null ? noti : this.noti);
		this.noti = null;

		return "customer";
	}

	@PostMapping("/addCustomer")
	public String addCustomer(@ModelAttribute("cus") CustomerDto cus, RedirectAttributes redirectAttributes) {

		try {
			List<Customer> existingCus = cusService.findAll();

			for (Customer row : existingCus) {
				if (row.getFirstName().equalsIgnoreCase(cus.getFirstName())
						&& row.getLastName().equalsIgnoreCase(cus.getLastName())) {
					NotificationModel noti = new NotificationModel();
					noti.setType(NotiType.WARNING);
					noti.setMsg("Customer " + cus.getFirstName() + " " + cus.getLastName() + " already exists!");
					this.noti = noti;

					return "redirect:";
				}
			}

			String faceNicImgPath = FilePath.getNicFilePath() + cus.getNic() + "_face.png";
			String backNicImgPath = FilePath.getNicFilePath() + cus.getNic() + "_back.png";
			try (FileOutputStream faceImgFile = new FileOutputStream(faceNicImgPath);
					FileOutputStream backImgFile = new FileOutputStream(backNicImgPath)) {

				MultipartFile faceNicImg = cus.getNicFaceImg();
				byte[] faceBytes = faceNicImg.getBytes();
				faceImgFile.write(faceBytes);

				MultipartFile backNicImg = cus.getNicBackImg();
				byte[] backBytes = backNicImg.getBytes();
				backImgFile.write(backBytes);

			} catch (Exception e) {
				LOG.error("Error occurred while saving NIC images for " + cus.getNic(), e);

				NotificationModel noti = new NotificationModel();
				noti.setType(NotiType.ERROR);
				noti.setMsg(
						"An error occurred while saving NIC images. Please contact the system administrator for further support.");
				this.noti = noti;

				return "redirect:";
			}

			Customer entity = new Customer();
			entity.setFirstName(cus.getFirstName());
			entity.setLastName(cus.getLastName());
			entity.setNic(cus.getNic());
			entity.setMobile(cus.getMobile());
			entity.setMobile2(cus.getMobile2());
			entity.setAddress(cus.getAddress());
			entity.setBillingAddress(cus.getBillingAddress());

			cusService.save(entity);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.SUCCESS);
			noti.setMsg("Customer " + cus.getFirstName() + " " + cus.getLastName() + " saved successfully!");
			this.noti = noti;

			return "redirect:";

		} catch (Exception e) {
			LOG.error("Error occurred while saving the customer", e);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.ERROR);
			noti.setMsg(
					"An error occurred while saving the customer. Please contact the system administrator for further support.");
			this.noti = noti;

			return "redirect:";
		}

	}

	@PostMapping("/editCustomer")
	public String editCustomer(@ModelAttribute("cus") CustomerDto cus) {
		try {
			List<Customer> existingCus = cusService.findAll();

			for (Customer row : existingCus) {
				if (row.getFirstName().equalsIgnoreCase(cus.getFirstName())
						&& row.getLastName().equalsIgnoreCase(cus.getLastName()) && row.getId() != cus.getId()) {
					NotificationModel noti = new NotificationModel();
					noti.setType(NotiType.WARNING);
					noti.setMsg("Customer " + cus.getFirstName() + " " + cus.getLastName() + " already exists in ID:"
							+ row.getId());
					this.noti = noti;

					return "redirect:";
				}
			}

			if (cus.getNicFaceImg() != null && !cus.getNicFaceImg().isEmpty()) {
				String faceNicImgPath = FilePath.getNicFilePath() + cus.getNic() + "_face.png";

				try (FileOutputStream faceImgFile = new FileOutputStream(faceNicImgPath)) {
					MultipartFile faceNicImg = cus.getNicFaceImg();
					byte[] faceBytes = faceNicImg.getBytes();
					faceImgFile.write(faceBytes);
				} catch (Exception e) {
					LOG.error("Error occurred while saving NIC images for " + cus.getNic(), e);

					NotificationModel noti = new NotificationModel();
					noti.setType(NotiType.ERROR);
					noti.setMsg(
							"An error occurred while saving NIC images. Please contact the system administrator for further support.");
					this.noti = noti;

					return "redirect:";
				}
			}

			if (cus.getNicBackImg() != null && !cus.getNicBackImg().isEmpty()) {
				String backNicImgPath = FilePath.getNicFilePath() + cus.getNic() + "_back.png";
				try (FileOutputStream backImgFile = new FileOutputStream(backNicImgPath)) {

					MultipartFile backNicImg = cus.getNicBackImg();
					byte[] backBytes = backNicImg.getBytes();
					backImgFile.write(backBytes);

				} catch (Exception e) {
					LOG.error("Error occurred while saving NIC images for " + cus.getNic(), e);

					NotificationModel noti = new NotificationModel();
					noti.setType(NotiType.ERROR);
					noti.setMsg(
							"An error occurred while saving NIC images. Please contact the system administrator for further support.");
					this.noti = noti;

					return "redirect:";
				}
			}

			Customer entity = new Customer();
			entity.setId(cus.getId());
			entity.setFirstName(cus.getFirstName());
			entity.setLastName(cus.getLastName());
			entity.setNic(cus.getNic());
			entity.setMobile(cus.getMobile());
			entity.setMobile2(cus.getMobile2());
			entity.setAddress(cus.getAddress());
			entity.setBillingAddress(cus.getBillingAddress());

			cusService.update(entity);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.SUCCESS);
			noti.setMsg("Customer " + cus.getFirstName() + " " + cus.getLastName() + " edited successfully!");
			this.noti = noti;

			return "redirect:";

		} catch (Exception e) {
			LOG.error("Error occurred while editing the customer", e);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.ERROR);
			noti.setMsg(
					"An error occurred while editing the customer. Please contact the system administrator for further support.");
			this.noti = noti;

			return "redirect:";
		}
	}

	@GetMapping("/deleteCustomer")
	public String deleteCustomer(@RequestParam("id") Long id) {
		try {
			// Don't delete the Customer from the DB. This customer may be used for rent and
			// reports. Hence, just mark the brand as inactive.
			Customer cus = cusService.getById(id);
			cus.setActive(false);
			cusService.save(cus);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.SUCCESS);
			noti.setMsg("Customer deleted successfully!");
			this.noti = noti;

			return "redirect:";

		} catch (Exception e) {
			LOG.error("Error occurred while deleting the customer", e);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.ERROR);
			noti.setMsg(
					"An error occurred while deleting the customer. Please contact the system administrator for further support.");
			this.noti = noti;

			return "redirect:";
		}
	}
}
