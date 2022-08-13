package com.kodak.controllers;

import java.math.BigDecimal;
import java.security.Principal;
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
import com.kodak.constants.RentItemStatus;
import com.kodak.constants.RentStatus;
import com.kodak.dtos.CustomerDto;
import com.kodak.dtos.RentItemFilterDto;
import com.kodak.models.Brand;
import com.kodak.models.Category;
import com.kodak.models.Customer;
import com.kodak.models.Employee;
import com.kodak.models.Item;
import com.kodak.models.Rent;
import com.kodak.models.RentCartItem;
import com.kodak.service.BrandService;
import com.kodak.service.CategoryService;
import com.kodak.service.CustomerService;
import com.kodak.service.EmployeeService;
import com.kodak.service.ItemService;
import com.kodak.service.RentCartItemService;
import com.kodak.service.RentService;
import com.kodak.utility.NotificationModel;

@Controller
@RequestMapping("/rent")
public class RentController {

	private static final Logger LOG = LoggerFactory.getLogger(RentController.class);

	@Autowired
	private CustomerService cusService;

	@Autowired
	private EmployeeService empService;

	@Autowired
	private ItemService itemService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private BrandService brandService;

	@Autowired
	private RentCartItemService rentCartItemService;

	@Autowired
	private RentService rentService;

	private NotificationModel noti;

	private boolean itemPage = false;

	@GetMapping()
	public String rent(Model model, Principal principal) {

		List<Customer> cusList = cusService.findAllActive();
		List<Item> itemList = itemService.findAllActive();
		List<Category> catList = categoryService.findAllActive();
		List<Brand> brandList = brandService.findAllActive();
		List<RentCartItem> cartItemList = rentCartItemService.findInprogressItemForUser(principal.getName());
		Rent rent = rentService.findInitialItemForEmployee(empService.findByUsername(principal.getName()));

		model.addAttribute("cusList", cusList);
		model.addAttribute("itemList", itemList);
		model.addAttribute("catList", catList);
		model.addAttribute("brandList", brandList);
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("rent", rent);

		// rent items filter form vlues
		RentItemFilterDto filter = new RentItemFilterDto();
		filter.setItemPage(false);
		filter.setCategory("Any");
		filter.setBrand("Any");
		filter.setStock("All");
		model.addAttribute("filter", filter);

		// If no any notification. assign empty
		NotificationModel noti = new NotificationModel();
		noti.setType(NotiType.NON);
		noti.setMsg("");
		model.addAttribute("noti", this.noti == null ? noti : this.noti);
		this.noti = null;

		model.addAttribute("itemPage", itemPage);

		return "rent";
	}

	@PostMapping("/rentItemsFilter")
	public String rentItemsFilter(@ModelAttribute("filter") RentItemFilterDto filter, Model model,
			Principal principal) {

		List<Item> itemList = new ArrayList<>();
		List<Item> itemListAll = itemService.findAllActive();

		for (Item item : itemListAll) {
			if ((filter.getCategory().equals("Any") || item.getCategory().getCatName().equals(filter.getCategory()))
					&& (filter.getBrand().equals("Any") || item.getBrand().getBrandName().equals(filter.getBrand()))
					&& (filter.getStock().equals("All")
							|| (filter.getStock().equals("In Stock") && item.getAvailable() > 0)
							|| (filter.getStock().equals("Out of Stock") && item.getAvailable() < 1))) {
				itemList.add(item);
			}
		}

		List<Customer> cusList = cusService.findAllActive();
		List<Category> catList = categoryService.findAllActive();
		List<Brand> brandList = brandService.findAllActive();
		List<RentCartItem> cartItemList = rentCartItemService.findInprogressItemForUser(principal.getName());

		model.addAttribute("cusList", cusList);
		model.addAttribute("itemList", itemList);
		model.addAttribute("catList", catList);
		model.addAttribute("brandList", brandList);
		model.addAttribute("cartItemList", cartItemList);

		// rent items filter form vlues
		filter.setItemPage(true);
		model.addAttribute("filter", filter);

		// If no any notification. assign empty
		NotificationModel noti = new NotificationModel();
		noti.setType(NotiType.NON);
		noti.setMsg("");
		model.addAttribute("noti", this.noti == null ? noti : this.noti);
		this.noti = null;

		itemPage = true;
		model.addAttribute("itemPage", itemPage);

		return "rent";
	}

	@PostMapping("/addItemToCart")
	public String addItemToCart(@ModelAttribute("item") Long itemId, @ModelAttribute("qty") int qty,
			Principal princial) {
		try {
			Item item = itemService.getById(itemId);

			rentCartItemService.addItemsToCart(item, qty, princial.getName());

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.SUCCESS);
			noti.setMsg(item.getItemName() + " added to cart successfully!");
			this.noti = noti;

		} catch (Exception e) {
			LOG.error("Error occurred while saving the branch", e);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.ERROR);
			noti.setMsg(
					"An error occurred while adding item to cart. Please contact the system administrator for further support.");
			this.noti = noti;
		}

		itemPage = true;

		return "redirect:";
	}

	@PostMapping("/updateItemQty")
	public String updateItemQty(@ModelAttribute("item") Long itemId, @ModelAttribute("qty") int qty,
			Principal princial) {
		try {
			Item item = itemService.getById(itemId);

			rentCartItemService.addItemsToCart(item, qty, princial.getName());
		} catch (Exception e) {
			LOG.error("Error occurred while saving the branch", e);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.ERROR);
			noti.setMsg(
					"An error occurred while adding item to cart. Please contact the system administrator for further support.");
			this.noti = noti;
		}

		itemPage = false;

		return "redirect:";
	}

	@PostMapping("/deleteCartItem")
	public String deleteCartItem(@ModelAttribute("cartId") Long cartId, Principal princial) {
		try {
			rentCartItemService.deleteById(cartId);

		} catch (Exception e) {
			LOG.error("Error occurred while saving the branch", e);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.ERROR);
			noti.setMsg(
					"An error occurred while adding item to cart. Please contact the system administrator for further support.");
			this.noti = noti;
		}

		itemPage = false;

		return "redirect:";
	}

	@PostMapping("/rentDetails")
	public String rentDetails(@ModelAttribute("startDate") String startDate, @ModelAttribute("endDate") String endDate,
			@ModelAttribute("cusId") Long cusId, Principal princial) {
		try {

			BigDecimal rentGrandTotal = new BigDecimal(0);
			BigDecimal rentTotalDeposit = new BigDecimal(0);

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			formatter = formatter.withLocale(Locale.getDefault());
			LocalDate sdate = LocalDate.parse(startDate, formatter);
			LocalDate edate = LocalDate.parse(endDate, formatter);

			int dates = edate.getDayOfYear() - sdate.getDayOfYear();

			List<RentCartItem> cartItemList = rentCartItemService.findInprogressItemForUser(princial.getName());

			for (RentCartItem cartItem : cartItemList) {
				Item item = cartItem.getItem();
				if (dates > 4) {
					BigDecimal subRentTotal = new BigDecimal(dates * item.getRentPrice5Days());
					cartItem.setSubRentTotal(subRentTotal);
					rentGrandTotal = rentGrandTotal.add(subRentTotal);

					BigDecimal subDepositTotal = new BigDecimal(dates * item.getDeposit5Days());
					cartItem.setSubDepositTotal(subDepositTotal);
					rentTotalDeposit = rentTotalDeposit.add(subDepositTotal);
				} else if (dates > 2) {
					BigDecimal subRentTotal = new BigDecimal(dates * item.getRentPrice3To4Days());
					cartItem.setSubRentTotal(subRentTotal);
					rentGrandTotal = rentGrandTotal.add(subRentTotal);

					BigDecimal subDepositTotal = new BigDecimal(dates * item.getDeposit3To4Days());
					cartItem.setSubDepositTotal(subDepositTotal);
					rentTotalDeposit = rentTotalDeposit.add(subDepositTotal);
				} else {
					BigDecimal subRentTotal = new BigDecimal(dates * item.getRentPrice1To2Days());
					cartItem.setSubRentTotal(subRentTotal);
					rentGrandTotal = rentGrandTotal.add(subRentTotal);

					BigDecimal subDepositTotal = new BigDecimal(dates * item.getDeposit1To2Days());
					cartItem.setSubDepositTotal(subDepositTotal);
					rentTotalDeposit = rentTotalDeposit.add(subDepositTotal);
				}

				cartItem.setDays(dates);
				cartItem.setSubTotal(cartItem.getSubRentTotal().add(cartItem.getSubDepositTotal()));
				rentCartItemService.update(cartItem);
			}

			Customer cus = cusService.getById(cusId);
			Employee emp = empService.findByUsername(princial.getName());

			// initiate rent job
			Rent rent = rentService.intiateRentJob(cus, emp, cartItemList, sdate, edate, rentGrandTotal,
					rentTotalDeposit);

			// update Cart item to rent job
			for (RentCartItem cartItem : cartItemList) {
				cartItem.setRent(rent);
				rentCartItemService.update(cartItem);
			}

		} catch (Exception e) {
			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.ERROR);
			noti.setMsg(
					"An error occurred while get rent details. Please contact the system administrator for further support.");
			this.noti = noti;
		}

		itemPage = false;

		return "redirect:";
	}

	@GetMapping("/completeRentJob")
	public String completeRentJob(Principal principal) {
		try {
			// Mark cart items complete
			List<RentCartItem> cartItemList = rentCartItemService.findInprogressItemForUser(principal.getName());
			for (RentCartItem cartItem : cartItemList) {
				cartItem.setStatus(RentItemStatus.COMPLETED.toString());
				rentCartItemService.update(cartItem);
			}

			// Mark rent complete
			Employee emp = empService.findByUsername(principal.getName());
			Rent rent = rentService.findInitialItemForEmployee(emp);
			rent.setStatus(RentStatus.RENTED.toString());
			rentService.update(rent);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.SUCCESS);
			noti.setMsg("Rent job complete successfully!");
			this.noti = noti;

		} catch (Exception e) {
			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.ERROR);
			noti.setMsg(
					"An error occurred while complete the rent job. Please contact the system administrator for further support.");
			this.noti = noti;
		}

		itemPage = true;

		return "redirect:";
	}

	@GetMapping("/cancelRentJob")
	public String cancelRentJob(Principal principal) {
		try {
			// Delete rent job
			// Mark cart items complete
			List<RentCartItem> cartItemList = rentCartItemService.findInprogressItemForUser(principal.getName());
			for (RentCartItem cartItem : cartItemList) {
				// reset the item available qty
				Item item = cartItem.getItem();
				item.setAvailable(item.getAvailable() + cartItem.getQty());
				itemService.update(item);

				cartItem.setStatus(RentItemStatus.CANCELED.toString());
				rentCartItemService.update(cartItem);
			}

			// Mark rent canceled
			Employee emp = empService.findByUsername(principal.getName());
			Rent rent = rentService.findInitialItemForEmployee(emp);
			rent.setStatus(RentStatus.CANCELED.toString());
			rentService.update(rent);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.SUCCESS);
			noti.setMsg("Rent job canceled successfully!");
			this.noti = noti;

		} catch (Exception e) {
			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.ERROR);
			noti.setMsg(
					"An error occurred while canceled the rent job. Please contact the system administrator for further support.");
			this.noti = noti;
		}

		itemPage = true;

		return "redirect:";
	}

	@GetMapping(value={"/ajaxCustomerName","/rent/ajaxCustomerName"})
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

	@GetMapping(value={"/ajaxCustomerPhone","/rent/ajaxCustomerPhone"})
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

	@GetMapping(value={"/ajaxCustomerNic","/rent/ajaxCustomerNic"})
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

	@GetMapping(value={"/ajaxCustomerByName","/rent/ajaxCustomerByName"})
	@ResponseBody
	public CustomerDto ajaxCustomerByName(@ModelAttribute("cusName") String cusName) {

		String[] names = cusName.split("\\s+");
		Customer cus = cusService.findByFirstNameAndLastName(names[0], names[1]);

		CustomerDto cusDto = new CustomerDto();
		cusDto.setId(cus.getId());
		cusDto.setFirstName(cus.getFirstName());
		cusDto.setLastName(cus.getLastName());
		cusDto.setMobile(cus.getMobile());
		cusDto.setNic(cus.getNic());
		cusDto.setBillingAddress(cus.getBillingAddress());

		return cusDto;
	}

	@GetMapping(value={"/ajaxCustomerByPhone","/rent/ajaxCustomerByPhone"})
	@ResponseBody
	public CustomerDto ajaxCustomerByPhone(@ModelAttribute("cusPhone") String cusPhone) {

		Customer cus = cusService.findByMobile(cusPhone);

		if (cus == null)
			cus = cusService.findByMobile2(cusPhone);

		CustomerDto cusDto = new CustomerDto();
		cusDto.setId(cus.getId());
		cusDto.setFirstName(cus.getFirstName());
		cusDto.setLastName(cus.getLastName());
		cusDto.setMobile(cus.getMobile());
		cusDto.setNic(cus.getNic());
		cusDto.setBillingAddress(cus.getBillingAddress());

		return cusDto;
	}

	@GetMapping(value={"/ajaxCustomerByNic","/rent/ajaxCustomerByNic"})
	@ResponseBody
	public CustomerDto ajaxCustomerByNic(@ModelAttribute("cusNic") String cusNic) {

		Customer cus = cusService.findByNic(cusNic);

		CustomerDto cusDto = new CustomerDto();
		cusDto.setId(cus.getId());
		cusDto.setFirstName(cus.getFirstName());
		cusDto.setLastName(cus.getLastName());
		cusDto.setMobile(cus.getMobile());
		cusDto.setNic(cus.getNic());
		cusDto.setBillingAddress(cus.getBillingAddress());

		return cusDto;
	}
}
