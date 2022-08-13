package com.kodak.controllers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kodak.constants.NotiType;
import com.kodak.constants.RentStatus;
import com.kodak.dtos.MostHiredItemReportFilterDto;
import com.kodak.dtos.ProfitItemReportFilterDto;
import com.kodak.dtos.ProfitRentReportDto;
import com.kodak.dtos.RentReportDto;
import com.kodak.models.Item;
import com.kodak.models.Rent;
import com.kodak.models.RentCartItem;
import com.kodak.service.RentService;
import com.kodak.utility.NotificationModel;

@Controller
@RequestMapping("/report")
public class ReportController {

	@Autowired
	private RentService rentService;

	private NotificationModel noti;

	@GetMapping("/profitByRent")
	public String profitByRent(Model model) {
		List<Rent> data = new ArrayList<>();
		Set<String> cusNameSet = new HashSet<>();
		BigDecimal rentTotal = new BigDecimal(0);
		BigDecimal lateTotal = new BigDecimal(0);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		formatter = formatter.withLocale(Locale.getDefault());
		int currentYear = LocalDate.now().getYear();
		LocalDate sDate = LocalDate.parse(currentYear + "-01-01", formatter);
		LocalDate eDate = LocalDate.parse(currentYear + "-12-31", formatter);

		List<Rent> rents = rentService.findByStatus(RentStatus.RETERNED.toString());

		for (Rent rent : rents) {
			if ((sDate.isEqual(rent.getStartDate()) || sDate.isBefore(rent.getStartDate()))
					&& (eDate.isEqual(rent.getEndDate()) || eDate.isAfter(rent.getEndDate()))) {
				data.add(rent);
				rentTotal = rentTotal.add(rent.getGrandRentTotal());
				lateTotal = lateTotal.add(rent.getLateChargers());
			}

			// for filter option list
			cusNameSet.add(rent.getCustomer().getFirstName() + " " + rent.getCustomer().getLastName());
		}

		model.addAttribute("rentList", data);
		model.addAttribute("rentTotal", rentTotal);
		model.addAttribute("lateTotal", lateTotal);
		model.addAttribute("profit", rentTotal.add(lateTotal));

		// Default filter values
		ProfitRentReportDto filter = new ProfitRentReportDto();
		filter.setCusName("All");
		filter.setCusNameList(new ArrayList<>(cusNameSet));
		filter.setStartDate(currentYear + "-01-01");
		filter.setEndDate(currentYear + "-12-31");
		model.addAttribute("filter", filter);

		// If no any notification. assign empty
		NotificationModel noti = new NotificationModel();
		noti.setType(NotiType.NON);
		noti.setMsg("");
		model.addAttribute("noti", this.noti == null ? noti : this.noti);
		this.noti = null;

		return "profitByRent";
	}

	@PostMapping("/filterByRent")
	public String filterByRent(@ModelAttribute("filter") ProfitRentReportDto filter, Model model) {
		List<Rent> data = new ArrayList<>();
		Set<String> cusNameSet = new HashSet<>();
		BigDecimal rentTotal = new BigDecimal(0);
		BigDecimal lateTotal = new BigDecimal(0);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		formatter = formatter.withLocale(Locale.getDefault());
		LocalDate sDate = LocalDate.parse(filter.getStartDate(), formatter);
		LocalDate eDate = LocalDate.parse(filter.getEndDate(), formatter);

		List<Rent> rents = rentService.findByStatus(RentStatus.RETERNED.toString());

		for (Rent rent : rents) {
			if ((sDate.isEqual(rent.getStartDate()) || sDate.isBefore(rent.getStartDate()))
					&& (eDate.isEqual(rent.getEndDate()) || eDate.isAfter(rent.getEndDate()))
					&& (filter.getCusName().endsWith("All") || filter.getCusName()
							.equals(rent.getCustomer().getFirstName() + " " + rent.getCustomer().getLastName()))) {
				data.add(rent);
				rentTotal = rentTotal.add(rent.getGrandRentTotal());
				lateTotal = lateTotal.add(rent.getLateChargers());
			}

			// for filter option list
			cusNameSet.add(rent.getCustomer().getFirstName() + " " + rent.getCustomer().getLastName());
		}

		model.addAttribute("rentList", data);
		model.addAttribute("rentTotal", rentTotal);
		model.addAttribute("lateTotal", lateTotal);
		model.addAttribute("profit", rentTotal.add(lateTotal));

		// Default filter values
		filter.setCusNameList(new ArrayList<>(cusNameSet));
		model.addAttribute("filter", filter);

		// If no any notification. assign empty
		NotificationModel noti = new NotificationModel();
		noti.setType(NotiType.NON);
		noti.setMsg("");
		model.addAttribute("noti", this.noti == null ? noti : this.noti);
		this.noti = null;

		return "profitByRent";
	}

	@GetMapping("/profitByItem")
	public String profitByItem(Model model) {

		Set<Integer> yearSet = new HashSet<>();
		Set<String> itemNameSet = new HashSet<>();

		BigDecimal jan = new BigDecimal(0), feb = new BigDecimal(0), mar = new BigDecimal(0), apr = new BigDecimal(0),
				may = new BigDecimal(0), jun = new BigDecimal(0), jul = new BigDecimal(0), aug = new BigDecimal(0),
				sep = new BigDecimal(0), oct = new BigDecimal(0), nov = new BigDecimal(0), dec = new BigDecimal(0);

		int currentYear = LocalDate.now().getYear();
		List<Rent> rents = rentService.findByStatus(RentStatus.RETERNED.toString());

		for (Rent rent : rents) {
			LocalDate sDate = rent.getStartDate();
			int year = sDate.getYear();
			int month = sDate.getMonthValue();

			if (year == currentYear) {
				if (month > 11) {
					dec = dec.add(rent.getGrandRentTotal());
					dec = dec.add(rent.getLateChargers());
				} else if (month > 10) {
					nov = nov.add(rent.getGrandRentTotal());
					nov = nov.add(rent.getLateChargers());
				} else if (month > 9) {
					oct = oct.add(rent.getGrandRentTotal());
					oct = oct.add(rent.getLateChargers());
				} else if (month > 8) {
					sep = sep.add(rent.getGrandRentTotal());
					sep = sep.add(rent.getLateChargers());
				} else if (month > 7) {
					aug = aug.add(rent.getGrandRentTotal());
					aug = aug.add(rent.getLateChargers());
				} else if (month > 6) {
					jul = jul.add(rent.getGrandRentTotal());
					jul = jul.add(rent.getLateChargers());
				} else if (month > 5) {
					jun = jun.add(rent.getGrandRentTotal());
					jun = jun.add(rent.getLateChargers());
				} else if (month > 4) {
					may = may.add(rent.getGrandRentTotal());
					may = may.add(rent.getLateChargers());
				} else if (month > 3) {
					apr = apr.add(rent.getGrandRentTotal());
					apr = apr.add(rent.getLateChargers());
				} else if (month > 2) {
					mar = mar.add(rent.getGrandRentTotal());
					mar = mar.add(rent.getLateChargers());
				} else if (month > 1) {
					feb = feb.add(rent.getGrandRentTotal());
					feb = feb.add(rent.getLateChargers());
				} else if (month > 0) {
					jan = jan.add(rent.getGrandRentTotal());
					jan = jan.add(rent.getLateChargers());
				}
			}

			// for filter option list
			yearSet.add(year);

			List<RentCartItem> cartItems = rent.getRentCartItemList();
			for (RentCartItem cartItem : cartItems) {
				Item item = cartItem.getItem();
				itemNameSet.add(item.getBrand().getBrandName() + " " + item.getItemName());
			}
		}

		LinkedHashMap<String, BigDecimal> graphData = new LinkedHashMap<>();
		graphData.put("Jan", jan);
		graphData.put("Feb", feb);
		graphData.put("Mar", mar);
		graphData.put("Apr", apr);
		graphData.put("May", may);
		graphData.put("Jun", jun);
		graphData.put("Jul", jul);
		graphData.put("Aug", aug);
		graphData.put("Sep", sep);
		graphData.put("Oct", oct);
		graphData.put("Nov", nov);
		graphData.put("Dec", dec);
		model.addAttribute("chartData", graphData);

		// Default filter values
		ProfitItemReportFilterDto filter = new ProfitItemReportFilterDto();
		filter.setYear(currentYear);
		filter.setItemName("All");
		filter.setYearList(new ArrayList<>(yearSet));
		filter.setItemNameList(new ArrayList<>(itemNameSet));
		model.addAttribute("filter", filter);

		// If no any notification. assign empty
		NotificationModel noti = new NotificationModel();
		noti.setType(NotiType.NON);
		noti.setMsg("");
		model.addAttribute("noti", this.noti == null ? noti : this.noti);
		this.noti = null;

		return "profitByItem";
	}

	@PostMapping("/filterByItem")
	public String filterByItem(@ModelAttribute("filter") ProfitItemReportFilterDto filter, Model model) {

		Set<Integer> yearSet = new HashSet<>();
		Set<String> itemNameSet = new HashSet<>();

		BigDecimal jan = new BigDecimal(0), feb = new BigDecimal(0), mar = new BigDecimal(0), apr = new BigDecimal(0),
				may = new BigDecimal(0), jun = new BigDecimal(0), jul = new BigDecimal(0), aug = new BigDecimal(0),
				sep = new BigDecimal(0), oct = new BigDecimal(0), nov = new BigDecimal(0), dec = new BigDecimal(0);

		List<Rent> rents = rentService.findByStatus(RentStatus.RETERNED.toString());

		for (Rent rent : rents) {
			LocalDate sDate = rent.getStartDate();
			int year = sDate.getYear();
			int month = sDate.getMonthValue();

			// for filter option list
			yearSet.add(year);

			List<RentCartItem> cartItems = rent.getRentCartItemList();
			for (RentCartItem cartItem : cartItems) {
				Item item = cartItem.getItem();

				// for filter option list
				itemNameSet.add(item.getBrand().getBrandName() + " " + item.getItemName());

				if (year == filter.getYear() && (filter.getItemName().equals("All")
						|| filter.getItemName().equals(item.getBrand().getBrandName() + " " + item.getItemName()))) {
					if (month > 11) {
						dec = dec.add(rent.getGrandRentTotal());
						dec = dec.add(rent.getLateChargers());
					} else if (month > 10) {
						nov = nov.add(rent.getGrandRentTotal());
						nov = nov.add(rent.getLateChargers());
					} else if (month > 9) {
						oct = oct.add(rent.getGrandRentTotal());
						oct = oct.add(rent.getLateChargers());
					} else if (month > 8) {
						sep = sep.add(rent.getGrandRentTotal());
						sep = sep.add(rent.getLateChargers());
					} else if (month > 7) {
						aug = aug.add(rent.getGrandRentTotal());
						aug = aug.add(rent.getLateChargers());
					} else if (month > 6) {
						jul = jul.add(rent.getGrandRentTotal());
						jul = jul.add(rent.getLateChargers());
					} else if (month > 5) {
						jun = jun.add(rent.getGrandRentTotal());
						jun = jun.add(rent.getLateChargers());
					} else if (month > 4) {
						may = may.add(rent.getGrandRentTotal());
						may = may.add(rent.getLateChargers());
					} else if (month > 3) {
						apr = apr.add(rent.getGrandRentTotal());
						apr = apr.add(rent.getLateChargers());
					} else if (month > 2) {
						mar = mar.add(rent.getGrandRentTotal());
						mar = mar.add(rent.getLateChargers());
					} else if (month > 1) {
						feb = feb.add(rent.getGrandRentTotal());
						feb = feb.add(rent.getLateChargers());
					} else if (month > 0) {
						jan = jan.add(rent.getGrandRentTotal());
						jan = jan.add(rent.getLateChargers());
					}
				}
			}
		}

		LinkedHashMap<String, BigDecimal> graphData = new LinkedHashMap<>();
		graphData.put("Jan", jan);
		graphData.put("Feb", feb);
		graphData.put("Mar", mar);
		graphData.put("Apr", apr);
		graphData.put("May", may);
		graphData.put("Jun", jun);
		graphData.put("Jul", jul);
		graphData.put("Aug", aug);
		graphData.put("Sep", sep);
		graphData.put("Oct", oct);
		graphData.put("Nov", nov);
		graphData.put("Dec", dec);
		model.addAttribute("chartData", graphData);

		// Default filter values
		filter.setYearList(new ArrayList<>(yearSet));
		filter.setItemNameList(new ArrayList<>(itemNameSet));
		model.addAttribute("filter", filter);

		// If no any notification. assign empty
		NotificationModel noti = new NotificationModel();
		noti.setType(NotiType.NON);
		noti.setMsg("");
		model.addAttribute("noti", this.noti == null ? noti : this.noti);
		this.noti = null;

		return "profitByItem";
	}

	@GetMapping("/rentReport")
	public String rentReport(Model model) {
		List<Rent> data = new ArrayList<>();
		Set<String> empNameSet = new HashSet<>();
		Set<String> cusNameSet = new HashSet<>();
		Set<String> statusSet = new HashSet<>();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		formatter = formatter.withLocale(Locale.getDefault());
		int currentYear = LocalDate.now().getYear();
		LocalDate sDate = LocalDate.parse(currentYear + "-01-01", formatter);
		LocalDate eDate = LocalDate.parse(currentYear + "-12-31", formatter);

		List<Rent> rents = rentService.findAll();

		for (Rent rent : rents) {
			if ((sDate.isEqual(rent.getStartDate()) || sDate.isBefore(rent.getStartDate()))
					&& (eDate.isEqual(rent.getEndDate()) || eDate.isAfter(rent.getEndDate()))) {
				data.add(rent);
			}

			// for filter option list
			empNameSet.add(rent.getEmployee().getFirstName() + " " + rent.getEmployee().getLastName());
			cusNameSet.add(rent.getCustomer().getFirstName() + " " + rent.getCustomer().getLastName());
			statusSet.add(rent.getStatus());
		}

		model.addAttribute("rentList", data);

		// Default filter values
		RentReportDto filter = new RentReportDto();
		filter.setUsername("All");
		filter.setUsernameList(new ArrayList<>(empNameSet));
		filter.setCusName("All");
		filter.setCusNameList(new ArrayList<>(cusNameSet));
		filter.setStartDate(currentYear + "-01-01");
		filter.setEndDate(currentYear + "-12-31");
		filter.setStatus("All");
		filter.setStatusList(new ArrayList<>(statusSet));
		model.addAttribute("filter", filter);

		// If no any notification. assign empty
		NotificationModel noti = new NotificationModel();
		noti.setType(NotiType.NON);
		noti.setMsg("");
		model.addAttribute("noti", this.noti == null ? noti : this.noti);
		this.noti = null;

		return "rentReport";
	}

	@PostMapping("/filterRentReport")
	public String filterRentReport(@ModelAttribute("filter") RentReportDto filter, Model model) {
		List<Rent> data = new ArrayList<>();
		Set<String> empNameSet = new HashSet<>();
		Set<String> cusNameSet = new HashSet<>();
		Set<String> statusSet = new HashSet<>();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		formatter = formatter.withLocale(Locale.getDefault());
		LocalDate sDate = LocalDate.parse(filter.getStartDate(), formatter);
		LocalDate eDate = LocalDate.parse(filter.getEndDate(), formatter);

		List<Rent> rents = rentService.findAll();

		for (Rent rent : rents) {
			if ((sDate.isEqual(rent.getStartDate()) || sDate.isBefore(rent.getStartDate()))
					&& (eDate.isEqual(rent.getEndDate()) || eDate.isAfter(rent.getEndDate()))
					&& (filter.getCusName().endsWith("All") || filter.getCusName()
							.equals(rent.getCustomer().getFirstName() + " " + rent.getCustomer().getLastName()))
					&& (filter.getUsername().endsWith("All") || filter.getUsername()
							.equals(rent.getEmployee().getFirstName() + " " + rent.getEmployee().getLastName()))
					&& (filter.getStatus().equals("All") || filter.getStatus().equals(rent.getStatus()))) {
				data.add(rent);
			}

			// for filter option list
			empNameSet.add(rent.getEmployee().getFirstName() + " " + rent.getEmployee().getLastName());
			cusNameSet.add(rent.getCustomer().getFirstName() + " " + rent.getCustomer().getLastName());
			statusSet.add(rent.getStatus());
		}

		model.addAttribute("rentList", data);

		// Default filter values
		filter.setUsernameList(new ArrayList<>(empNameSet));
		filter.setCusNameList(new ArrayList<>(cusNameSet));
		filter.setStatusList(new ArrayList<>(statusSet));
		model.addAttribute("filter", filter);

		// If no any notification. assign empty
		NotificationModel noti = new NotificationModel();
		noti.setType(NotiType.NON);
		noti.setMsg("");
		model.addAttribute("noti", this.noti == null ? noti : this.noti);
		this.noti = null;

		return "rentReport";
	}

	@GetMapping("/mostHiredItem")
	public String mostHiredItem(Model model) {

		LinkedHashMap<String, Integer> graphData = new LinkedHashMap<>();
		Set<Integer> yearSet = new HashSet<>();

		int currentYear = LocalDate.now().getYear();
		List<Rent> rents = rentService.findByStatus(RentStatus.RETERNED.toString());

		for (Rent rent : rents) {
			LocalDate sDate = rent.getStartDate();
			int year = sDate.getYear();

			// for filter option list
			yearSet.add(year);

			if (currentYear == year) {
				List<RentCartItem> cartItems = rent.getRentCartItemList();
				for (RentCartItem cartItem : cartItems) {
					Item item = cartItem.getItem();
					String itemName = item.getItemName();

					if (graphData.containsKey(itemName)) {
						int preCount = graphData.get(itemName);
						graphData.put(itemName, preCount + cartItem.getQty());
					} else {
						graphData.put(itemName, cartItem.getQty());
					}
				}
			}
		}

		model.addAttribute("chartData", graphData);

		// Default filter values
		MostHiredItemReportFilterDto filter = new MostHiredItemReportFilterDto();
		filter.setYear(currentYear);
		filter.setMonth("All");
		filter.setYearList(new ArrayList<>(yearSet));
		model.addAttribute("filter", filter);

		// If no any notification. assign empty
		NotificationModel noti = new NotificationModel();
		noti.setType(NotiType.NON);
		noti.setMsg("");
		model.addAttribute("noti", this.noti == null ? noti : this.noti);
		this.noti = null;

		return "mostHiredItems";
	}

	@PostMapping("/filterMostHiredItem")
	public String filterMostHiredItem(@ModelAttribute("filter") MostHiredItemReportFilterDto filter, Model model) {
		LinkedHashMap<String, Integer> graphData = new LinkedHashMap<>();
		Set<Integer> yearSet = new HashSet<>();

		int filterYear = filter.getYear();
		String filterMonth = filter.getMonth();
		List<Rent> rents = rentService.findByStatus(RentStatus.RETERNED.toString());

		for (Rent rent : rents) {
			LocalDate sDate = rent.getStartDate();
			int year = sDate.getYear();
			int month = sDate.getMonthValue();

			// for filter option list
			yearSet.add(year);

			if (filterYear == year && (filterMonth.equals("All") || (filterMonth.equals("January") && month == 1)
					|| (filterMonth.equals("February") && month == 2) || (filterMonth.equals("March") && month == 3)
					|| (filterMonth.equals("April") && month == 4) || (filterMonth.equals("May") && month == 5)
					|| (filterMonth.equals("June") && month == 6) || (filterMonth.equals("July") && month == 7)
					|| (filterMonth.equals("August") && month == 8) || (filterMonth.equals("September") && month == 9)
					|| (filterMonth.equals("October") && month == 10) || (filterMonth.equals("November") && month == 11)
					|| (filterMonth.equals("December") && month == 12))) {
				List<RentCartItem> cartItems = rent.getRentCartItemList();
				for (RentCartItem cartItem : cartItems) {
					Item item = cartItem.getItem();
					String itemName = item.getItemName();

					if (graphData.containsKey(itemName)) {
						int preCount = graphData.get(itemName);
						graphData.put(itemName, preCount + cartItem.getQty());
					} else {
						graphData.put(itemName, cartItem.getQty());
					}
				}
			}
		}

		model.addAttribute("chartData", graphData);

		// Default filter values
		filter.setYearList(new ArrayList<>(yearSet));
		model.addAttribute("filter", filter);

		// If no any notification. assign empty
		NotificationModel noti = new NotificationModel();
		noti.setType(NotiType.NON);
		noti.setMsg("");
		model.addAttribute("noti", this.noti == null ? noti : this.noti);
		this.noti = null;

		return "mostHiredItems";
	}
}
