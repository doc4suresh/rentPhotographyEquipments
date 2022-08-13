package com.kodak.controllers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kodak.constants.NotiType;
import com.kodak.constants.RentStatus;
import com.kodak.models.Customer;
import com.kodak.models.Item;
import com.kodak.models.Rent;
import com.kodak.models.RentCartItem;
import com.kodak.service.CustomerService;
import com.kodak.service.ItemService;
import com.kodak.service.RentService;
import com.kodak.utility.NotificationModel;

@Controller
@RequestMapping("/return")
public class ReturnController {

	private static final Logger LOG = LoggerFactory.getLogger(ReturnController.class);

	@Autowired
	private RentService rentService;

	@Autowired
	private CustomerService cusService;

	@Autowired
	private ItemService itemService;

	private NotificationModel noti;

	@GetMapping
	public String getReturn(Model model) {
		List<Rent> rents = new ArrayList<>();
		model.addAttribute("rents", rents);

		// If no any notification. assign empty
		NotificationModel noti = new NotificationModel();
		noti.setType(NotiType.NON);
		noti.setMsg("");
		model.addAttribute("noti", this.noti == null ? noti : this.noti);
		this.noti = null;

		return "return";
	}

	@GetMapping("/getRentsByCusName")
	public String getRentsByCusName(@ModelAttribute("cusName") String cusName, Model model) {
		try {
			String[] firstLastName = cusName.split(" ");
			Customer cus = cusService.findByFirstNameAndLastName(firstLastName[0], firstLastName[1]);
			List<Rent> rents = rentService.findOnGoingRentJobsByCustomer(cus);

			model.addAttribute("rents", rents);

			NotificationModel noti = new NotificationModel();
			if (rents.size() > 0) {
				// If no any notification. assign empty
				noti.setType(NotiType.NON);
				noti.setMsg("");
				model.addAttribute("noti", this.noti == null ? noti : this.noti);
			} else {
				//
				noti.setType(NotiType.WARNING);
				noti.setMsg("No any pending rents for " + cusName);
				model.addAttribute("noti", noti);
			}
		} catch (Exception e) {
			LOG.error("Error occured when get Rents by Customer Name", e);
		}

		this.noti = null;

		return "return";
	}

	@GetMapping("/getRentsByCusPhone")
	public String getRentsByCusPhone(@ModelAttribute("cusPhone") String cusPhone, Model model) {
		try {
			Customer cus = cusService.findByMobile(cusPhone);
			if (cus == null)
				cus = cusService.findByMobile2(cusPhone);

			List<Rent> rents = rentService.findOnGoingRentJobsByCustomer(cus);

			model.addAttribute("rents", rents);

			NotificationModel noti = new NotificationModel();
			if (rents.size() > 0) {
				// If no any notification. assign empty
				noti.setType(NotiType.NON);
				noti.setMsg("");
				model.addAttribute("noti", this.noti == null ? noti : this.noti);
			} else {
				//
				noti.setType(NotiType.WARNING);
				noti.setMsg("No any pending rents for " + cus.getFirstName() + " " + cus.getLastName());
				model.addAttribute("noti", noti);
			}
		} catch (Exception e) {
			LOG.error("Error occured when get Rents by Customer Phone", e);
		}

		this.noti = null;

		return "return";
	}

	@GetMapping("/getRentsByCusNic")
	public String getRentsByCusNic(@ModelAttribute("cusNic") String cusNic, Model model) {

		try {
			Customer cus = cusService.findByNic(cusNic);

			List<Rent> rents = rentService.findOnGoingRentJobsByCustomer(cus);

			model.addAttribute("rents", rents);

			NotificationModel noti = new NotificationModel();
			if (rents.size() > 0) {
				// If no any notification. assign empty
				noti.setType(NotiType.NON);
				noti.setMsg("");
				model.addAttribute("noti", this.noti == null ? noti : this.noti);
			} else {
				//
				noti.setType(NotiType.WARNING);
				noti.setMsg("No any pending rents for " + cus.getFirstName() + " " + cus.getLastName());
				model.addAttribute("noti", noti);
			}
		} catch (Exception e) {
			LOG.error("Error occured when get Rents by Customer NIC", e);
		}

		this.noti = null;

		return "return";
	}

	@PostMapping("/completeReturn")
	public String completeReturn(@ModelAttribute("rentId") long rentId,
			@ModelAttribute("returnedDate") String returnedDate, @ModelAttribute("lateChargers") String lateChargers,
			@ModelAttribute("damageFine") String damageFine) {

		try {
			Rent rent = rentService.getById(rentId);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			formatter = formatter.withLocale(Locale.getDefault());
			LocalDate returnDate = LocalDate.parse(returnedDate, formatter);
			rent.setReturnedDate(returnDate);
			rent.setLateChargers(
					lateChargers != null && !lateChargers.isEmpty() ? new BigDecimal(lateChargers) : new BigDecimal(0));
			rent.setDamageFine(
					damageFine != null && !damageFine.isEmpty() ? new BigDecimal(damageFine) : new BigDecimal(0));
			rent.setItemsReturned(true);
			rent.setDepositReturned(true);
			rent.setStatus(RentStatus.RETERNED.toString());
			rentService.update(rent);

			// Reset Item available qty
			List<RentCartItem> cartItems = rent.getRentCartItemList();
			for (RentCartItem cartItem : cartItems) {
				Item item = cartItem.getItem();
				item.setAvailable(item.getAvailable() + cartItem.getQty());
				itemService.update(item);
			}

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.SUCCESS);
			noti.setMsg("Item returned successfully!");
			this.noti = noti;

		} catch (Exception e) {
			LOG.error("Error occurred while return item", e);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.ERROR);
			noti.setMsg(
					"An error occurred while return items. Please contact the system administrator for further support.");
			this.noti = noti;
		}

		return "redirect:";
	}
	
	@GetMapping(value={"/ajaxCustomerName","/return/ajaxCustomerName"})
	@ResponseBody
	public List<String> ajaxCustomerName(@ModelAttribute("q") String q) {
		List<String> results = new ArrayList<>();

		List<Customer> cusList = cusService.findAllActive();
		for (Customer cus : cusList) {
			if (cus.getFirstName().contains(q) || cus.getLastName().contains(q)) {
				results.add(cus.getFirstName() + " " + cus.getLastName());
			}
		}

		return results;
	}

	@GetMapping(value={"/ajaxCustomerPhone","/return/ajaxCustomerPhone"})
	@ResponseBody
	public List<String> ajaxCustomerPhone(@ModelAttribute("q") String q) {
		List<String> results = new ArrayList<>();

		List<Customer> cusList = cusService.findAllActive();
		for (Customer cus : cusList) {
			if (cus.getMobile().contains(q)) {
				results.add(cus.getMobile());
			}
			if (cus.getMobile2().contains(q)) {
				results.add(cus.getMobile2());
			}
		}

		return results;
	}

	@GetMapping(value={"/ajaxCustomerNic","/return/ajaxCustomerNic"})
	@ResponseBody
	public List<String> ajaxCustomerNic(@ModelAttribute("q") String q) {
		List<String> results = new ArrayList<>();

		List<Customer> cusList = cusService.findAllActive();
		for (Customer cus : cusList) {
			if (cus.getNic().contains(q)) {
				results.add(cus.getNic());
			}
		}

		return results;
	}

	@GetMapping(value={"/ajaxGetLateCharge","/return/ajaxGetLateCharge"})
	@ResponseBody
	public BigDecimal ajaxGetLateCharge(@ModelAttribute("rentId") long rentId, @ModelAttribute("date") String date) {

		try {

			Rent rent = rentService.getById(rentId);

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			formatter = formatter.withLocale(Locale.getDefault());
			LocalDate returnDate = LocalDate.parse(date, formatter);
			LocalDate strartDate = rent.getStartDate();

			int dates = returnDate.getDayOfYear() - strartDate.getDayOfYear();
			BigDecimal lateChargers = new BigDecimal(0);
			BigDecimal totalLateChargers = new BigDecimal(0);

			List<RentCartItem> cartItems = rent.getRentCartItemList();

			for (RentCartItem cartItem : cartItems) {
				Item item = cartItem.getItem();
				if (dates > 4) {
					BigDecimal subRentTotal = new BigDecimal(dates * item.getRentPrice5Days());
					lateChargers = subRentTotal.subtract(cartItem.getSubRentTotal());
				} else if (dates > 2) {
					BigDecimal subRentTotal = new BigDecimal(dates * item.getRentPrice3To4Days());
					lateChargers = subRentTotal.subtract(cartItem.getSubRentTotal());
				} else {
					BigDecimal subRentTotal = new BigDecimal(dates * item.getRentPrice1To2Days());
					lateChargers = subRentTotal.subtract(cartItem.getSubRentTotal());
				}

				totalLateChargers = totalLateChargers.add(lateChargers);
			}

			return totalLateChargers;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new BigDecimal(0);
	}
}
