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
import com.kodak.dtos.ItemDto;
import com.kodak.models.Brand;
import com.kodak.models.Category;
import com.kodak.models.Item;
import com.kodak.models.Item3dObjectDetection;
import com.kodak.models.ItemBattery;
import com.kodak.models.ItemCameraSpecs;
import com.kodak.models.ItemCharger;
import com.kodak.models.ItemExposureControl;
import com.kodak.models.ItemFlash;
import com.kodak.models.ItemFlightControlSystem;
import com.kodak.models.ItemFocus;
import com.kodak.models.ItemGeneralSpecs;
import com.kodak.models.ItemImaging;
import com.kodak.models.ItemInterface;
import com.kodak.models.ItemPackagingInfo;
import com.kodak.models.ItemPerformance;
import com.kodak.models.ItemRemoteControllerOrTransmitter;
import com.kodak.models.ItemVideoSpecs;
import com.kodak.models.ItemViewfinderMonitor;
import com.kodak.models.ItemVisionSystem;
import com.kodak.service.BrandService;
import com.kodak.service.CategoryService;
import com.kodak.service.Item3dObjectDetectionService;
import com.kodak.service.ItemBatteryService;
import com.kodak.service.ItemCameraSpecsService;
import com.kodak.service.ItemChargerService;
import com.kodak.service.ItemExposureControlService;
import com.kodak.service.ItemFlashService;
import com.kodak.service.ItemFlightControlSystemService;
import com.kodak.service.ItemFocusService;
import com.kodak.service.ItemGeneralSpecsService;
import com.kodak.service.ItemImagingService;
import com.kodak.service.ItemInterfaceService;
import com.kodak.service.ItemPackagingInfoService;
import com.kodak.service.ItemPerformanceService;
import com.kodak.service.ItemRemoteControllerOrTransmitterService;
import com.kodak.service.ItemService;
import com.kodak.service.ItemVideoSpecsService;
import com.kodak.service.ItemViewfinderMonitorService;
import com.kodak.service.ItemVisionSystemService;
import com.kodak.utility.FilePath;
import com.kodak.utility.NotificationModel;

@Controller
@RequestMapping("/item")
public class ItemController {

	private static final Logger LOG = LoggerFactory.getLogger(ItemController.class);

	@Autowired
	private ItemService itemService;

	@Autowired
	private CategoryService catService;

	@Autowired
	private BrandService brandService;

	@Autowired
	private Item3dObjectDetectionService item3dObjectDetectionService;

	@Autowired
	private ItemBatteryService itemBatteryService;

	@Autowired
	private ItemCameraSpecsService itemCameraSpecsService;

	@Autowired
	private ItemChargerService itemChargerService;

	@Autowired
	private ItemExposureControlService itemExposureControlService;

	@Autowired
	private ItemFlashService itemFlashService;

	@Autowired
	private ItemFlightControlSystemService itemFlightControlSystemService;

	@Autowired
	private ItemFocusService itemFocusService;

	@Autowired
	private ItemGeneralSpecsService itemGeneralSpecsService;

	@Autowired
	private ItemImagingService itemImagingService;

	@Autowired
	private ItemInterfaceService itemInterfaceService;

	@Autowired
	private ItemPackagingInfoService itemPackagingInfoService;

	@Autowired
	private ItemPerformanceService itemPerformanceService;

	@Autowired
	private ItemRemoteControllerOrTransmitterService itemRemoteControllerOrTransmitterService;

	@Autowired
	private ItemVideoSpecsService itemVideoSpecsService;

	@Autowired
	private ItemViewfinderMonitorService itemViewfinderMonitorService;

	@Autowired
	private ItemVisionSystemService itemVisionSystemService;

	private NotificationModel noti;

	@GetMapping()
	public String item(Model model) {
		Item item = new Item();
		List<Item> itemList = itemService.findAllActive();
		List<Category> catList = catService.findAllActive();
		List<Brand> brandList = brandService.findAllActive();

		model.addAttribute("item", item);
		model.addAttribute("itemList", itemList);
		model.addAttribute("catList", catList);
		model.addAttribute("brandList", brandList);

		// If no any notification. assign empty
		NotificationModel noti = new NotificationModel();
		noti.setType(NotiType.NON);
		noti.setMsg("");
		model.addAttribute("noti", this.noti == null ? noti : this.noti);
		this.noti = null;
		
		return "item";
	}

	@PostMapping("/addItem")
	public String addItem(@ModelAttribute("item") ItemDto itemDto, RedirectAttributes redirectAttributes, Model model) {

		try {
			List<Item> existItemList = itemService.findAll();

			for (Item existItem : existItemList) {
				if (existItem.getItemName().equalsIgnoreCase(itemDto.getItemName())
						|| existItem.getItemCode().equalsIgnoreCase(itemDto.getItemCode())) {
					NotificationModel noti = new NotificationModel();
					noti.setType(NotiType.WARNING);
					noti.setMsg("Item " + itemDto.getItemName() + " already exists!");
					this.noti = noti;

					return "redirect:";
				}
			}

			Item item = new Item();
			item.setItemCode(itemDto.getItemCode());
			item.setItemName(itemDto.getItemName());
			item.setKeyFeatures(itemDto.getKeyFeatures());
			item.setOverview(itemDto.getOverview());
			item.setQty(itemDto.getQty());
			item.setAvailable(itemDto.getQty());
			item.setRentPrice1To2Days(itemDto.getRentPrice1To2Days());
			item.setRentPrice3To4Days(itemDto.getRentPrice3To4Days());
			item.setRentPrice5Days(itemDto.getRentPrice5Days());
			item.setDeposit1To2Days(itemDto.getDeposit1To2Days());
			item.setDeposit3To4Days(itemDto.getDeposit3To4Days());
			item.setDeposit5Days(itemDto.getDeposit5Days());

			Category cat = catService.getByCatName(itemDto.getCategory());
			item.setCategory(cat);

			Brand brand = brandService.getByBrandName(itemDto.getBrand());
			item.setBrand(brand);

			item = itemService.save(item);

			// Save item image to disk
			String itemImgPath = FilePath.getItemImgPath() + item.getId() + ".png";
			try (FileOutputStream faceImgFile = new FileOutputStream(itemImgPath)) {

				MultipartFile itemImg = itemDto.getItemImg();
				byte[] faceBytes = itemImg.getBytes();
				faceImgFile.write(faceBytes);
			} catch (Exception e) {
				LOG.error("Error occurred while saving Item images for " + itemDto.getItemName(), e);
			}

			// save Item 3d Object Detection Specs
			if (!itemDto.getOperatingRange().isEmpty()) {
				Item3dObjectDetection item3d = new Item3dObjectDetection();
				item3d.setOperatingRange(itemDto.getOperatingRange());
				item3d.setItem(item);
				item3dObjectDetectionService.save(item3d);
			}

			// save Item Battery Specs
			ItemBattery battery = new ItemBattery();
			boolean hasBattery = false;
			if (!itemDto.getChemistry().isEmpty()) {
				battery.setChemistry(itemDto.getChemistry());
				hasBattery = true;
			}
			if (!itemDto.getCapacity().isEmpty()) {
				battery.setCapacity(itemDto.getCapacity());
				hasBattery = true;
			}
			if (!itemDto.getConfiguration().isEmpty()) {
				battery.setConfiguration(itemDto.getConfiguration());
				hasBattery = true;
			}
			if (!itemDto.getBatteryOperatingTemperature().isEmpty()) {
				battery.setBatteryOperatingTemperature(itemDto.getBatteryOperatingTemperature());
				hasBattery = true;
			}
			if (hasBattery) {
				battery.setItem(item);
				itemBatteryService.save(battery);
			}

			// save Item Item Camera Specs
			ItemCameraSpecs camera = new ItemCameraSpecs();
			boolean hasCamera = false;
			if (!itemDto.getSensor().isEmpty()) {
				camera.setSensor(itemDto.getSensor());
				hasCamera = true;
			}
			if (!itemDto.getEffectivePixels().isEmpty()) {
				camera.setEffectivePixels(itemDto.getEffectivePixels());
				hasCamera = true;
			}
			if (!itemDto.getLensFieldofView().isEmpty()) {
				camera.setLensFieldofView(itemDto.getLensFieldofView());
				hasCamera = true;
			}
			if (!itemDto.getAperture().isEmpty()) {
				camera.setAperture(itemDto.getAperture());
				hasCamera = true;
			}
			if (!itemDto.getMinimumFocusingDistance().isEmpty()) {
				camera.setMinimumFocusingDistance(itemDto.getMinimumFocusingDistance());
				hasCamera = true;
			}
			if (!itemDto.getPhotoISORange().isEmpty()) {
				camera.setPhotoISORange(itemDto.getPhotoISORange());
				hasCamera = true;
			}
			if (!itemDto.getElectronicShutterSpeed().isEmpty()) {
				camera.setElectronicShutterSpeed(itemDto.getElectronicShutterSpeed());
				hasCamera = true;
			}
			if (!itemDto.getPhotoFormat().isEmpty()) {
				camera.setPhotoFormat(itemDto.getPhotoFormat());
				hasCamera = true;
			}
			if (!itemDto.getVideoFormat().isEmpty()) {
				camera.setVideoFormat(itemDto.getVideoFormat());
				hasCamera = true;
			}
			if (hasCamera) {
				camera.setItem(item);
				itemCameraSpecsService.save(camera);
			}

			// Save Item Charger Specs
			ItemCharger charger = new ItemCharger();
			boolean hasCharger = false;
			if (!itemDto.getAcInputPower().isEmpty()) {
				charger.setAcInputPower(itemDto.getAcInputPower());
				hasCharger = true;
			}
			if (!itemDto.getOutputVoltage().isEmpty()) {
				charger.setOutputVoltage(itemDto.getOutputVoltage());
				hasCharger = true;
			}
			if (!itemDto.getPowerRating().isEmpty()) {
				charger.setPowerRating(itemDto.getPowerRating());
				hasCharger = true;
			}
			if (hasCharger) {
				charger.setItem(item);
				itemChargerService.save(charger);
			}

			// Save Item Exposure Control Specs
			ItemExposureControl expo = new ItemExposureControl();
			boolean hasExpo = false;
			if (!itemDto.getExposureControl().isEmpty()) {
				expo.setExposureControl(itemDto.getExposureControl());
				hasExpo = true;
			}
			if (!itemDto.getExposureFlashModes().isEmpty()) {
				expo.setExposureFlashModes(itemDto.getExposureFlashModes());
				hasExpo = true;
			}
			if (!itemDto.getIsoSensitivity().isEmpty()) {
				expo.setIsoSensitivity(itemDto.getIsoSensitivity());
				hasExpo = true;
			}
			if (!itemDto.getMeteringMethod().isEmpty()) {
				expo.setMeteringMethod(itemDto.getMeteringMethod());
				hasExpo = true;
			}
			if (!itemDto.getExposureModes().isEmpty()) {
				expo.setExposureModes(itemDto.getExposureModes());
				hasExpo = true;
			}
			if (!itemDto.getExposureCompensation().isEmpty()) {
				expo.setExposureCompensation(itemDto.getExposureCompensation());
				hasExpo = true;
			}
			if (!itemDto.getMeteringRange().isEmpty()) {
				expo.setMeteringRange(itemDto.getMeteringRange());
				hasExpo = true;
			}
			if (!itemDto.getWhiteBalance().isEmpty()) {
				expo.setWhiteBalance(itemDto.getWhiteBalance());
				hasExpo = true;
			}
			if (!itemDto.getSelfTimer().isEmpty()) {
				expo.setSelfTimer(itemDto.getSelfTimer());
				hasExpo = true;
			}
			if (!itemDto.getShutterSpeed().isEmpty()) {
				expo.setShutterSpeed(itemDto.getShutterSpeed());
				hasExpo = true;
			}
			if (!itemDto.getContinuousShooting().isEmpty()) {
				expo.setContinuousShooting(itemDto.getContinuousShooting());
				hasExpo = true;
			}
			if (hasExpo) {
				expo.setItem(item);
				itemExposureControlService.save(expo);
			}

			// Save Item Flash Specs
			ItemFlash flash = new ItemFlash();
			boolean hasFlash = false;
			if (!itemDto.getBuiltInFlash().isEmpty()) {
				flash.setBuiltInFlash(itemDto.getBuiltInFlash());
				hasFlash = true;
			}
			if (!itemDto.getDedicatedFlashSystem().isEmpty()) {
				flash.setDedicatedFlashSystem(itemDto.getDedicatedFlashSystem());
				hasFlash = true;
			}
			if (!itemDto.getExternalFlashConnection().isEmpty()) {
				flash.setExternalFlashConnection(itemDto.getExternalFlashConnection());
				hasFlash = true;
			}
			if (!itemDto.getFlashModes().isEmpty()) {
				flash.setFlashModes(itemDto.getFlashModes());
				hasFlash = true;
			}
			if (!itemDto.getMaximumSyncSpeed().isEmpty()) {
				flash.setMaximumSyncSpeed(itemDto.getMaximumSyncSpeed());
				hasFlash = true;
			}
			if (!itemDto.getFlashCompensation().isEmpty()) {
				flash.setFlashCompensation(itemDto.getFlashCompensation());
				hasFlash = true;
			}
			if (hasFlash) {
				flash.setItem(item);
				itemFlashService.save(flash);
			}

			// Save Item Flight Control System Specs
			if (!itemDto.getGnssSupport().isEmpty()) {
				ItemFlightControlSystem flight = new ItemFlightControlSystem();
				flight.setGnssSupport(itemDto.getGnssSupport());
				flight.setItem(item);
				itemFlightControlSystemService.save(flight);
			}

			// Save Item Focus Specs
			ItemFocus focus = new ItemFocus();
			boolean hasFocus = false;
			if (!itemDto.getFocusType().isEmpty()) {
				focus.setFocusType(itemDto.getFocusType());
				hasFocus = true;
			}
			if (!itemDto.getFocusMode().isEmpty()) {
				focus.setFocusMode(itemDto.getFocusMode());
				hasFocus = true;
			}
			if (!itemDto.getAutofocusPoints().isEmpty()) {
				focus.setAutofocusPoints(itemDto.getAutofocusPoints());
				hasFocus = true;
			}
			if (!itemDto.getAutofocusSensitivity().isEmpty()) {
				focus.setAutofocusSensitivity(itemDto.getAutofocusSensitivity());
				hasFocus = true;
			}
			if (hasFocus) {
				focus.setItem(item);
				itemFocusService.save(focus);
			}

			// Save Item General Specs
			ItemGeneralSpecs general = new ItemGeneralSpecs();
			boolean hasGeneral = false;
			if (!itemDto.getNumberOfRotors().isEmpty()) {
				general.setNumberOfRotors(itemDto.getNumberOfRotors());
				hasGeneral = true;
			}
			if (!itemDto.getOperatingTemperature().isEmpty()) {
				general.setOperatingTemperature(itemDto.getOperatingTemperature());
				hasGeneral = true;
			}
			if (!itemDto.getOverallDimensions().isEmpty()) {
				general.setOverallDimensions(itemDto.getOverallDimensions());
				hasGeneral = true;
			}
			if (hasGeneral) {
				general.setItem(item);
				itemGeneralSpecsService.save(general);
			}

			// Save Item Imaging Specs
			ItemImaging imaging = new ItemImaging();
			boolean hasImaging = false;
			if (!itemDto.getLensMount().isEmpty()) {
				imaging.setLensMount(itemDto.getLensMount());
				hasImaging = true;
			}
			if (!itemDto.getCameraFormat().isEmpty()) {
				imaging.setCameraFormat(itemDto.getCameraFormat());
				hasImaging = true;
			}
			if (!itemDto.getPixels().isEmpty()) {
				imaging.setPixels(itemDto.getPixels());
				hasImaging = true;
			}
			if (!itemDto.getMaximumResolution().isEmpty()) {
				imaging.setMaximumResolution(itemDto.getMaximumResolution());
				hasImaging = true;
			}
			if (!itemDto.getAspectRatio().isEmpty()) {
				imaging.setAspectRatio(itemDto.getAspectRatio());
				hasImaging = true;
			}
			if (!itemDto.getSensorType().isEmpty()) {
				imaging.setSensorType(itemDto.getSensorType());
				hasImaging = true;
			}
			if (!itemDto.getSensorSize().isEmpty()) {
				imaging.setSensorSize(itemDto.getSensorSize());
				hasImaging = true;
			}
			if (!itemDto.getImageFileFormat().isEmpty()) {
				imaging.setImageFileFormat(itemDto.getImageFileFormat());
				hasImaging = true;
			}
			if (!itemDto.getBitDepth().isEmpty()) {
				imaging.setBitDepth(itemDto.getBitDepth());
				hasImaging = true;
			}
			if (!itemDto.getImageStabilization().isEmpty()) {
				imaging.setImageStabilization(itemDto.getImageStabilization());
				hasImaging = true;
			}
			if (hasImaging) {
				imaging.setItem(item);
				itemImagingService.save(imaging);
			}

			// Save Item Interface Specs
			ItemInterface inter = new ItemInterface();
			boolean hasInter = false;
			if (!itemDto.getMemoryCardSlot().isEmpty()) {
				inter.setMemoryCardSlot(itemDto.getMemoryCardSlot());
				hasInter = true;
			}
			if (!itemDto.getInterfaceConnectivity().isEmpty()) {
				inter.setInterfaceConnectivity(itemDto.getInterfaceConnectivity());
				hasInter = true;
			}
			if (!itemDto.getWireless().isEmpty()) {
				inter.setWireless(itemDto.getWireless());
				hasInter = true;
			}
			if (hasInter) {
				inter.setItem(item);
				itemInterfaceService.save(inter);
			}

			// Save Item Packaging Info Specs
			ItemPackagingInfo pack = new ItemPackagingInfo();
			boolean hasPack = false;
			if (!itemDto.getPackageWeight().isEmpty()) {
				pack.setPackageWeight(itemDto.getPackageWeight());
				hasPack = true;
			}
			if (!itemDto.getDimensions().isEmpty()) {
				pack.setDimensions(itemDto.getDimensions());
				hasPack = true;
			}
			if (hasPack) {
				pack.setItem(item);
				itemPackagingInfoService.save(pack);
			}

			// Save Item Performance Specs
			ItemPerformance performance = new ItemPerformance();
			boolean hasPerformance = false;
			if (!itemDto.getMaximumTakeoffWeight().isEmpty()) {
				performance.setMaximumTakeoffWeight(itemDto.getMaximumTakeoffWeight());
				hasPerformance = true;
			}
			if (!itemDto.getMaximumHorizontalSpeed().isEmpty()) {
				performance.setMaximumHorizontalSpeed(itemDto.getMaximumHorizontalSpeed());
				hasPerformance = true;
			}
			if (!itemDto.getMaximumAscentSpeed().isEmpty()) {
				performance.setMaximumAscentSpeed(itemDto.getMaximumAscentSpeed());
				hasPerformance = true;
			}
			if (!itemDto.getMaximumDescentSpeed().isEmpty()) {
				performance.setMaximumDescentSpeed(itemDto.getMaximumDescentSpeed());
				hasPerformance = true;
			}
			if (!itemDto.getMaximumWindResistance().isEmpty()) {
				performance.setMaximumWindResistance(itemDto.getMaximumWindResistance());
				hasPerformance = true;
			}
			if (!itemDto.getFlightCeiling().isEmpty()) {
				performance.setFlightCeiling(itemDto.getFlightCeiling());
				hasPerformance = true;
			}
			if (!itemDto.getMaximumFlightTime().isEmpty()) {
				performance.setMaximumFlightTime(itemDto.getMaximumFlightTime());
				hasPerformance = true;
			}
			if (!itemDto.getMaximumHoverTime().isEmpty()) {
				performance.setMaximumHoverTime(itemDto.getMaximumHoverTime());
				hasPerformance = true;
			}
			if (!itemDto.getMaximumTiltAngle().isEmpty()) {
				performance.setMaximumTiltAngle(itemDto.getMaximumTiltAngle());
				hasPerformance = true;
			}
			if (!itemDto.getHoveringAccuracy().isEmpty()) {
				performance.setHoveringAccuracy(itemDto.getHoveringAccuracy());
				hasPerformance = true;
			}
			if (hasPerformance) {
				performance.setItem(item);
				itemPerformanceService.save(performance);
			}

			// Save Item Remote Controller or Transmitter Specs
			ItemRemoteControllerOrTransmitter remote = new ItemRemoteControllerOrTransmitter();
			boolean hasRemote = false;
			if (!itemDto.getOperatingFrequency().isEmpty()) {
				remote.setOperatingFrequency(itemDto.getOperatingFrequency());
				hasRemote = true;
			}
			if (!itemDto.getMaximumOperatingDistance().isEmpty()) {
				remote.setMaximumOperatingDistance(itemDto.getMaximumOperatingDistance());
				hasRemote = true;
			}
			if (!itemDto.getTransmitterPower().isEmpty()) {
				remote.setTransmitterPower(itemDto.getTransmitterPower());
				hasRemote = true;
			}
			if (!itemDto.getConnectivity().isEmpty()) {
				remote.setConnectivity(itemDto.getConnectivity());
				hasRemote = true;
			}
			if (hasRemote) {
				remote.setItem(item);
				itemRemoteControllerOrTransmitterService.save(remote);
			}

			// Save Item VideoSpecs Specs
			ItemVideoSpecs video = new ItemVideoSpecs();
			boolean hasVideo = false;
			if (!itemDto.getRecordingModes().isEmpty()) {
				video.setRecordingModes(itemDto.getRecordingModes());
				hasVideo = true;
			}
			if (!itemDto.getExternalRecordingModes().isEmpty()) {
				video.setExternalRecordingModes(itemDto.getExternalRecordingModes());
				hasVideo = true;
			}
			if (!itemDto.getRecordingLimit().isEmpty()) {
				video.setRecordingLimit(itemDto.getRecordingLimit());
				hasVideo = true;
			}
			if (!itemDto.getVideoEncoding().isEmpty()) {
				video.setVideoEncoding(itemDto.getVideoEncoding());
				hasVideo = true;
			}
			if (!itemDto.getAudioRecording().isEmpty()) {
				video.setAudioRecording(itemDto.getAudioRecording());
				hasVideo = true;
			}
			if (!itemDto.getAudioFileFormat().isEmpty()) {
				video.setAudioFileFormat(itemDto.getAudioFileFormat());
				hasVideo = true;
			}
			if (hasVideo) {
				video.setItem(item);
				itemVideoSpecsService.save(video);
			}

			// Save Item View finder Monitor Specs
			ItemViewfinderMonitor view = new ItemViewfinderMonitor();
			boolean hasView = false;
			if (!itemDto.getViewfinderType().isEmpty()) {
				view.setViewfinderType(itemDto.getViewfinderType());
				hasView = true;
			}
			if (!itemDto.getViewfinderSize().isEmpty()) {
				view.setViewfinderSize(itemDto.getViewfinderSize());
				hasView = true;
			}
			if (!itemDto.getViewfinderResolution().isEmpty()) {
				view.setViewfinderResolution(itemDto.getViewfinderResolution());
				hasView = true;
			}
			if (!itemDto.getViewfinderEyePoint().isEmpty()) {
				view.setViewfinderEyePoint(itemDto.getViewfinderEyePoint());
				hasView = true;
			}
			if (!itemDto.getViewfinderCoverage().isEmpty()) {
				view.setViewfinderCoverage(itemDto.getViewfinderCoverage());
				hasView = true;
			}
			if (!itemDto.getViewfinderMagnification().isEmpty()) {
				view.setViewfinderMagnification(itemDto.getViewfinderMagnification());
				hasView = true;
			}
			if (!itemDto.getDiopterAdjustment().isEmpty()) {
				view.setDiopterAdjustment(itemDto.getDiopterAdjustment());
				hasView = true;
			}
			if (!itemDto.getMonitorSize().isEmpty()) {
				view.setMonitorSize(itemDto.getMonitorSize());
				hasView = true;
			}
			if (!itemDto.getMonitorResolution().isEmpty()) {
				view.setMonitorResolution(itemDto.getMonitorResolution());
				hasView = true;
			}
			if (!itemDto.getMonitorType().isEmpty()) {
				view.setMonitorType(itemDto.getMonitorType());
				hasView = true;
			}
			if (hasView) {
				view.setItem(item);
				itemViewfinderMonitorService.save(view);
			}

			// Save Item Vision System Specs
			ItemVisionSystem vision = new ItemVisionSystem();
			boolean hasVision = false;
			if (!itemDto.getVisionSystem().isEmpty()) {
				vision.setVisionSystem(itemDto.getVisionSystem());
				hasVision = true;
			}
			if (!itemDto.getObstacleSensoryRange().isEmpty()) {
				vision.setObstacleSensoryRange(itemDto.getObstacleSensoryRange());
				hasVision = true;
			}
			if (!itemDto.getForwardFieldOfView().isEmpty()) {
				vision.setForwardFieldOfView(itemDto.getForwardFieldOfView());
				hasVision = true;
			}
			if (!itemDto.getDownwardFieldOfView().isEmpty()) {
				vision.setDownwardFieldOfView(itemDto.getDownwardFieldOfView());
				hasVision = true;
			}
			if (!itemDto.getBackwardFieldOfView().isEmpty()) {
				vision.setBackwardFieldOfView(itemDto.getBackwardFieldOfView());
				hasVision = true;
			}
			if (!itemDto.getOperatingEnvironment().isEmpty()) {
				vision.setOperatingEnvironment(itemDto.getOperatingEnvironment());
				hasVision = true;
			}
			if (hasVision) {
				vision.setItem(item);
				itemVisionSystemService.save(vision);
			}

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.SUCCESS);
			noti.setMsg("Item " + itemDto.getItemName() + " saved successfully!");
			this.noti = noti;

		} catch (

		Exception e) {
			LOG.error("Error occurred while saving the Item", e);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.ERROR);
			noti.setMsg(
					"An error occurred while saving the Item. Please contact the system administrator for further support.");
			this.noti = noti;
		}

		return "redirect:";
	}

	@PostMapping("/editItem")
	public String editItem(@ModelAttribute("item") ItemDto itemDto, RedirectAttributes redirectAttributes,
			Model model) {

		try {
			List<Item> existItemList = itemService.findAll();

			for (Item existItem : existItemList) {
				if ((existItem.getItemName().equalsIgnoreCase(itemDto.getItemName())
						|| existItem.getItemCode().equalsIgnoreCase(itemDto.getItemCode()))
						&& existItem.getId() != itemDto.getId()) {
					NotificationModel noti = new NotificationModel();
					noti.setType(NotiType.WARNING);
					noti.setMsg("Item " + itemDto.getItemName() + " already exists!");
					this.noti = noti;

					return "redirect:";
				}
			}

			Item item = new Item();
			item.setId(itemDto.getId());
			item.setItemCode(itemDto.getItemCode());
			item.setItemName(itemDto.getItemName());
			item.setKeyFeatures(itemDto.getKeyFeatures());
			item.setOverview(itemDto.getOverview());
			item.setQty(itemDto.getQty());
			item.setAvailable(itemDto.getAvailable());
			item.setRentPrice1To2Days(itemDto.getRentPrice1To2Days());
			item.setRentPrice3To4Days(itemDto.getRentPrice3To4Days());
			item.setRentPrice5Days(itemDto.getRentPrice5Days());
			item.setDeposit1To2Days(itemDto.getDeposit1To2Days());
			item.setDeposit3To4Days(itemDto.getDeposit3To4Days());
			item.setDeposit5Days(itemDto.getDeposit5Days());

			Category cat = catService.getByCatName(itemDto.getCategory());
			item.setCategory(cat);

			Brand brand = brandService.getByBrandName(itemDto.getBrand());
			item.setBrand(brand);

			item = itemService.save(item);

			// Save item image to disk
			if (itemDto.getItemImg() != null && !itemDto.getItemImg().isEmpty()) {
				String itemImgPath = FilePath.getItemImgPath() + item.getId() + ".png";
				try (FileOutputStream faceImgFile = new FileOutputStream(itemImgPath)) {

					MultipartFile itemImg = itemDto.getItemImg();
					byte[] faceBytes = itemImg.getBytes();
					faceImgFile.write(faceBytes);
				} catch (Exception e) {
					LOG.error("Error occurred while saving Item images for " + itemDto.getItemName(), e);
				}
			}

			// save Item 3d Object Detection Specs
			if (!itemDto.getOperatingRange().isEmpty()) {
				Item3dObjectDetection item3d = new Item3dObjectDetection();
				item3d.setId(itemDto.getItem3dObjectDetectionId());
				item3d.setOperatingRange(itemDto.getOperatingRange());
				item3d.setItem(item);
				item3dObjectDetectionService.save(item3d);
			}

			// save Item Battery Specs
			ItemBattery battery = new ItemBattery();
			boolean hasBattery = false;
			if (!itemDto.getChemistry().isEmpty()) {
				battery.setChemistry(itemDto.getChemistry());
				hasBattery = true;
			}
			if (!itemDto.getCapacity().isEmpty()) {
				battery.setCapacity(itemDto.getCapacity());
				hasBattery = true;
			}
			if (!itemDto.getConfiguration().isEmpty()) {
				battery.setConfiguration(itemDto.getConfiguration());
				hasBattery = true;
			}
			if (!itemDto.getBatteryOperatingTemperature().isEmpty()) {
				battery.setBatteryOperatingTemperature(itemDto.getBatteryOperatingTemperature());
				hasBattery = true;
			}
			if (hasBattery) {
				battery.setId(itemDto.getItemBatteryId());
				battery.setItem(item);
				itemBatteryService.save(battery);
			}

			// save Item Item Camera Specs
			ItemCameraSpecs camera = new ItemCameraSpecs();
			boolean hasCamera = false;
			if (!itemDto.getSensor().isEmpty()) {
				camera.setSensor(itemDto.getSensor());
				hasCamera = true;
			}
			if (!itemDto.getEffectivePixels().isEmpty()) {
				camera.setEffectivePixels(itemDto.getEffectivePixels());
				hasCamera = true;
			}
			if (!itemDto.getLensFieldofView().isEmpty()) {
				camera.setLensFieldofView(itemDto.getLensFieldofView());
				hasCamera = true;
			}
			if (!itemDto.getAperture().isEmpty()) {
				camera.setAperture(itemDto.getAperture());
				hasCamera = true;
			}
			if (!itemDto.getMinimumFocusingDistance().isEmpty()) {
				camera.setMinimumFocusingDistance(itemDto.getMinimumFocusingDistance());
				hasCamera = true;
			}
			if (!itemDto.getPhotoISORange().isEmpty()) {
				camera.setPhotoISORange(itemDto.getPhotoISORange());
				hasCamera = true;
			}
			if (!itemDto.getElectronicShutterSpeed().isEmpty()) {
				camera.setElectronicShutterSpeed(itemDto.getElectronicShutterSpeed());
				hasCamera = true;
			}
			if (!itemDto.getPhotoFormat().isEmpty()) {
				camera.setPhotoFormat(itemDto.getPhotoFormat());
				hasCamera = true;
			}
			if (!itemDto.getVideoFormat().isEmpty()) {
				camera.setVideoFormat(itemDto.getVideoFormat());
				hasCamera = true;
			}
			if (hasCamera) {
				camera.setId(itemDto.getItemCameraSpecsId());
				camera.setItem(item);
				itemCameraSpecsService.save(camera);
			}

			// Save Item Charger Specs
			ItemCharger charger = new ItemCharger();
			boolean hasCharger = false;
			if (!itemDto.getAcInputPower().isEmpty()) {
				charger.setAcInputPower(itemDto.getAcInputPower());
				hasCharger = true;
			}
			if (!itemDto.getOutputVoltage().isEmpty()) {
				charger.setOutputVoltage(itemDto.getOutputVoltage());
				hasCharger = true;
			}
			if (!itemDto.getPowerRating().isEmpty()) {
				charger.setPowerRating(itemDto.getPowerRating());
				hasCharger = true;
			}
			if (hasCharger) {
				charger.setId(itemDto.getItemChargerId());
				charger.setItem(item);
				itemChargerService.save(charger);
			}

			// Save Item Exposure Control Specs
			ItemExposureControl expo = new ItemExposureControl();
			boolean hasExpo = false;
			if (!itemDto.getExposureControl().isEmpty()) {
				expo.setExposureControl(itemDto.getExposureControl());
				hasExpo = true;
			}
			if (!itemDto.getExposureFlashModes().isEmpty()) {
				expo.setExposureFlashModes(itemDto.getExposureFlashModes());
				hasExpo = true;
			}
			if (!itemDto.getIsoSensitivity().isEmpty()) {
				expo.setIsoSensitivity(itemDto.getIsoSensitivity());
				hasExpo = true;
			}
			if (!itemDto.getMeteringMethod().isEmpty()) {
				expo.setMeteringMethod(itemDto.getMeteringMethod());
				hasExpo = true;
			}
			if (!itemDto.getExposureModes().isEmpty()) {
				expo.setExposureModes(itemDto.getExposureModes());
				hasExpo = true;
			}
			if (!itemDto.getExposureCompensation().isEmpty()) {
				expo.setExposureCompensation(itemDto.getExposureCompensation());
				hasExpo = true;
			}
			if (!itemDto.getMeteringRange().isEmpty()) {
				expo.setMeteringRange(itemDto.getMeteringRange());
				hasExpo = true;
			}
			if (!itemDto.getWhiteBalance().isEmpty()) {
				expo.setWhiteBalance(itemDto.getWhiteBalance());
				hasExpo = true;
			}
			if (!itemDto.getSelfTimer().isEmpty()) {
				expo.setSelfTimer(itemDto.getSelfTimer());
				hasExpo = true;
			}
			if (!itemDto.getShutterSpeed().isEmpty()) {
				expo.setShutterSpeed(itemDto.getShutterSpeed());
				hasExpo = true;
			}
			if (!itemDto.getContinuousShooting().isEmpty()) {
				expo.setContinuousShooting(itemDto.getContinuousShooting());
				hasExpo = true;
			}
			if (hasExpo) {
				expo.setId(itemDto.getItemExposureControlId());
				expo.setItem(item);
				itemExposureControlService.save(expo);
			}

			// Save Item Flash Specs
			ItemFlash flash = new ItemFlash();
			boolean hasFlash = false;
			if (!itemDto.getBuiltInFlash().isEmpty()) {
				flash.setBuiltInFlash(itemDto.getBuiltInFlash());
				hasFlash = true;
			}
			if (!itemDto.getDedicatedFlashSystem().isEmpty()) {
				flash.setDedicatedFlashSystem(itemDto.getDedicatedFlashSystem());
				hasFlash = true;
			}
			if (!itemDto.getExternalFlashConnection().isEmpty()) {
				flash.setExternalFlashConnection(itemDto.getExternalFlashConnection());
				hasFlash = true;
			}
			if (!itemDto.getFlashModes().isEmpty()) {
				flash.setFlashModes(itemDto.getFlashModes());
				hasFlash = true;
			}
			if (!itemDto.getMaximumSyncSpeed().isEmpty()) {
				flash.setMaximumSyncSpeed(itemDto.getMaximumSyncSpeed());
				hasFlash = true;
			}
			if (!itemDto.getFlashCompensation().isEmpty()) {
				flash.setFlashCompensation(itemDto.getFlashCompensation());
				hasFlash = true;
			}
			if (hasFlash) {
				flash.setId(itemDto.getItemFlashId());
				flash.setItem(item);
				itemFlashService.save(flash);
			}

			// Save Item Flight Control System Specs
			if (!itemDto.getGnssSupport().isEmpty()) {
				ItemFlightControlSystem flight = new ItemFlightControlSystem();
				flight.setId(itemDto.getItemFlightControlSystemId());
				flight.setGnssSupport(itemDto.getGnssSupport());
				flight.setItem(item);
				itemFlightControlSystemService.save(flight);
			}

			// Save Item Focus Specs
			ItemFocus focus = new ItemFocus();
			boolean hasFocus = false;
			if (!itemDto.getFocusType().isEmpty()) {
				focus.setFocusType(itemDto.getFocusType());
				hasFocus = true;
			}
			if (!itemDto.getFocusMode().isEmpty()) {
				focus.setFocusMode(itemDto.getFocusMode());
				hasFocus = true;
			}
			if (!itemDto.getAutofocusPoints().isEmpty()) {
				focus.setAutofocusPoints(itemDto.getAutofocusPoints());
				hasFocus = true;
			}
			if (!itemDto.getAutofocusSensitivity().isEmpty()) {
				focus.setAutofocusSensitivity(itemDto.getAutofocusSensitivity());
				hasFocus = true;
			}
			if (hasFocus) {
				focus.setId(itemDto.getItemFocusId());
				focus.setItem(item);
				itemFocusService.save(focus);
			}

			// Save Item General Specs
			ItemGeneralSpecs general = new ItemGeneralSpecs();
			boolean hasGeneral = false;
			if (!itemDto.getNumberOfRotors().isEmpty()) {
				general.setNumberOfRotors(itemDto.getNumberOfRotors());
				hasGeneral = true;
			}
			if (!itemDto.getOperatingTemperature().isEmpty()) {
				general.setOperatingTemperature(itemDto.getOperatingTemperature());
				hasGeneral = true;
			}
			if (!itemDto.getOverallDimensions().isEmpty()) {
				general.setOverallDimensions(itemDto.getOverallDimensions());
				hasGeneral = true;
			}
			if (hasGeneral) {
				general.setId(itemDto.getItemGeneralSpecsId());
				general.setItem(item);
				itemGeneralSpecsService.save(general);
			}

			// Save Item Imaging Specs
			ItemImaging imaging = new ItemImaging();
			boolean hasImaging = false;
			if (!itemDto.getLensMount().isEmpty()) {
				imaging.setLensMount(itemDto.getLensMount());
				hasImaging = true;
			}
			if (!itemDto.getCameraFormat().isEmpty()) {
				imaging.setCameraFormat(itemDto.getCameraFormat());
				hasImaging = true;
			}
			if (!itemDto.getPixels().isEmpty()) {
				imaging.setPixels(itemDto.getPixels());
				hasImaging = true;
			}
			if (!itemDto.getMaximumResolution().isEmpty()) {
				imaging.setMaximumResolution(itemDto.getMaximumResolution());
				hasImaging = true;
			}
			if (!itemDto.getAspectRatio().isEmpty()) {
				imaging.setAspectRatio(itemDto.getAspectRatio());
				hasImaging = true;
			}
			if (!itemDto.getSensorType().isEmpty()) {
				imaging.setSensorType(itemDto.getSensorType());
				hasImaging = true;
			}
			if (!itemDto.getSensorSize().isEmpty()) {
				imaging.setSensorSize(itemDto.getSensorSize());
				hasImaging = true;
			}
			if (!itemDto.getImageFileFormat().isEmpty()) {
				imaging.setImageFileFormat(itemDto.getImageFileFormat());
				hasImaging = true;
			}
			if (!itemDto.getBitDepth().isEmpty()) {
				imaging.setBitDepth(itemDto.getBitDepth());
				hasImaging = true;
			}
			if (!itemDto.getImageStabilization().isEmpty()) {
				imaging.setImageStabilization(itemDto.getImageStabilization());
				hasImaging = true;
			}
			if (hasImaging) {
				imaging.setId(itemDto.getItemImagingId());
				imaging.setItem(item);
				itemImagingService.save(imaging);
			}

			// Save Item Interface Specs
			ItemInterface inter = new ItemInterface();
			boolean hasInter = false;
			if (!itemDto.getMemoryCardSlot().isEmpty()) {
				inter.setMemoryCardSlot(itemDto.getMemoryCardSlot());
				hasInter = true;
			}
			if (!itemDto.getInterfaceConnectivity().isEmpty()) {
				inter.setInterfaceConnectivity(itemDto.getInterfaceConnectivity());
				hasInter = true;
			}
			if (!itemDto.getWireless().isEmpty()) {
				inter.setWireless(itemDto.getWireless());
				hasInter = true;
			}
			if (hasInter) {
				inter.setId(itemDto.getItemInterfaceId());
				inter.setItem(item);
				itemInterfaceService.save(inter);
			}

			// Save Item Packaging Info Specs
			ItemPackagingInfo pack = new ItemPackagingInfo();
			boolean hasPack = false;
			if (!itemDto.getPackageWeight().isEmpty()) {
				pack.setPackageWeight(itemDto.getPackageWeight());
				hasPack = true;
			}
			if (!itemDto.getDimensions().isEmpty()) {
				pack.setDimensions(itemDto.getDimensions());
				hasPack = true;
			}
			if (hasPack) {
				pack.setId(itemDto.getItemPackagingInfoId());
				pack.setItem(item);
				itemPackagingInfoService.save(pack);
			}

			// Save Item Performance Specs
			ItemPerformance performance = new ItemPerformance();
			boolean hasPerformance = false;
			if (!itemDto.getMaximumTakeoffWeight().isEmpty()) {
				performance.setMaximumTakeoffWeight(itemDto.getMaximumTakeoffWeight());
				hasPerformance = true;
			}
			if (!itemDto.getMaximumHorizontalSpeed().isEmpty()) {
				performance.setMaximumHorizontalSpeed(itemDto.getMaximumHorizontalSpeed());
				hasPerformance = true;
			}
			if (!itemDto.getMaximumAscentSpeed().isEmpty()) {
				performance.setMaximumAscentSpeed(itemDto.getMaximumAscentSpeed());
				hasPerformance = true;
			}
			if (!itemDto.getMaximumDescentSpeed().isEmpty()) {
				performance.setMaximumDescentSpeed(itemDto.getMaximumDescentSpeed());
				hasPerformance = true;
			}
			if (!itemDto.getMaximumWindResistance().isEmpty()) {
				performance.setMaximumWindResistance(itemDto.getMaximumWindResistance());
				hasPerformance = true;
			}
			if (!itemDto.getFlightCeiling().isEmpty()) {
				performance.setFlightCeiling(itemDto.getFlightCeiling());
				hasPerformance = true;
			}
			if (!itemDto.getMaximumFlightTime().isEmpty()) {
				performance.setMaximumFlightTime(itemDto.getMaximumFlightTime());
				hasPerformance = true;
			}
			if (!itemDto.getMaximumHoverTime().isEmpty()) {
				performance.setMaximumHoverTime(itemDto.getMaximumHoverTime());
				hasPerformance = true;
			}
			if (!itemDto.getMaximumTiltAngle().isEmpty()) {
				performance.setMaximumTiltAngle(itemDto.getMaximumTiltAngle());
				hasPerformance = true;
			}
			if (!itemDto.getHoveringAccuracy().isEmpty()) {
				performance.setHoveringAccuracy(itemDto.getHoveringAccuracy());
				hasPerformance = true;
			}
			if (hasPerformance) {
				performance.setId(itemDto.getItemPerformanceId());
				performance.setItem(item);
				itemPerformanceService.save(performance);
			}

			// Save Item Remote Controller or Transmitter Specs
			ItemRemoteControllerOrTransmitter remote = new ItemRemoteControllerOrTransmitter();
			boolean hasRemote = false;
			if (!itemDto.getOperatingFrequency().isEmpty()) {
				remote.setOperatingFrequency(itemDto.getOperatingFrequency());
				hasRemote = true;
			}
			if (!itemDto.getMaximumOperatingDistance().isEmpty()) {
				remote.setMaximumOperatingDistance(itemDto.getMaximumOperatingDistance());
				hasRemote = true;
			}
			if (!itemDto.getTransmitterPower().isEmpty()) {
				remote.setTransmitterPower(itemDto.getTransmitterPower());
				hasRemote = true;
			}
			if (!itemDto.getConnectivity().isEmpty()) {
				remote.setConnectivity(itemDto.getConnectivity());
				hasRemote = true;
			}
			if (hasRemote) {
				remote.setId(itemDto.getItemRemoteControllerOrTransmitterId());
				remote.setItem(item);
				itemRemoteControllerOrTransmitterService.save(remote);
			}

			// Save Item VideoSpecs Specs
			ItemVideoSpecs video = new ItemVideoSpecs();
			boolean hasVideo = false;
			if (!itemDto.getRecordingModes().isEmpty()) {
				video.setRecordingModes(itemDto.getRecordingModes());
				hasVideo = true;
			}
			if (!itemDto.getExternalRecordingModes().isEmpty()) {
				video.setExternalRecordingModes(itemDto.getExternalRecordingModes());
				hasVideo = true;
			}
			if (!itemDto.getRecordingLimit().isEmpty()) {
				video.setRecordingLimit(itemDto.getRecordingLimit());
				hasVideo = true;
			}
			if (!itemDto.getVideoEncoding().isEmpty()) {
				video.setVideoEncoding(itemDto.getVideoEncoding());
				hasVideo = true;
			}
			if (!itemDto.getAudioRecording().isEmpty()) {
				video.setAudioRecording(itemDto.getAudioRecording());
				hasVideo = true;
			}
			if (!itemDto.getAudioFileFormat().isEmpty()) {
				video.setAudioFileFormat(itemDto.getAudioFileFormat());
				hasVideo = true;
			}
			if (hasVideo) {
				video.setId(itemDto.getItemVideoSpecsId());
				video.setItem(item);
				itemVideoSpecsService.save(video);
			}

			// Save Item View finder Monitor Specs
			ItemViewfinderMonitor view = new ItemViewfinderMonitor();
			boolean hasView = false;
			if (!itemDto.getViewfinderType().isEmpty()) {
				view.setViewfinderType(itemDto.getViewfinderType());
				hasView = true;
			}
			if (!itemDto.getViewfinderSize().isEmpty()) {
				view.setViewfinderSize(itemDto.getViewfinderSize());
				hasView = true;
			}
			if (!itemDto.getViewfinderResolution().isEmpty()) {
				view.setViewfinderResolution(itemDto.getViewfinderResolution());
				hasView = true;
			}
			if (!itemDto.getViewfinderEyePoint().isEmpty()) {
				view.setViewfinderEyePoint(itemDto.getViewfinderEyePoint());
				hasView = true;
			}
			if (!itemDto.getViewfinderCoverage().isEmpty()) {
				view.setViewfinderCoverage(itemDto.getViewfinderCoverage());
				hasView = true;
			}
			if (!itemDto.getViewfinderMagnification().isEmpty()) {
				view.setViewfinderMagnification(itemDto.getViewfinderMagnification());
				hasView = true;
			}
			if (!itemDto.getDiopterAdjustment().isEmpty()) {
				view.setDiopterAdjustment(itemDto.getDiopterAdjustment());
				hasView = true;
			}
			if (!itemDto.getMonitorSize().isEmpty()) {
				view.setMonitorSize(itemDto.getMonitorSize());
				hasView = true;
			}
			if (!itemDto.getMonitorResolution().isEmpty()) {
				view.setMonitorResolution(itemDto.getMonitorResolution());
				hasView = true;
			}
			if (!itemDto.getMonitorType().isEmpty()) {
				view.setMonitorType(itemDto.getMonitorType());
				hasView = true;
			}
			if (hasView) {
				view.setId(itemDto.getItemViewfinderMonitorId());
				view.setItem(item);
				itemViewfinderMonitorService.save(view);
			}

			// Save Item Vision System Specs
			ItemVisionSystem vision = new ItemVisionSystem();
			boolean hasVision = false;
			if (!itemDto.getVisionSystem().isEmpty()) {
				vision.setVisionSystem(itemDto.getVisionSystem());
				hasVision = true;
			}
			if (!itemDto.getObstacleSensoryRange().isEmpty()) {
				vision.setObstacleSensoryRange(itemDto.getObstacleSensoryRange());
				hasVision = true;
			}
			if (!itemDto.getForwardFieldOfView().isEmpty()) {
				vision.setForwardFieldOfView(itemDto.getForwardFieldOfView());
				hasVision = true;
			}
			if (!itemDto.getDownwardFieldOfView().isEmpty()) {
				vision.setDownwardFieldOfView(itemDto.getDownwardFieldOfView());
				hasVision = true;
			}
			if (!itemDto.getBackwardFieldOfView().isEmpty()) {
				vision.setBackwardFieldOfView(itemDto.getBackwardFieldOfView());
				hasVision = true;
			}
			if (!itemDto.getOperatingEnvironment().isEmpty()) {
				vision.setOperatingEnvironment(itemDto.getOperatingEnvironment());
				hasVision = true;
			}
			if (hasVision) {
				vision.setId(itemDto.getItemVisionSystemId());
				vision.setItem(item);
				itemVisionSystemService.save(vision);
			}

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.SUCCESS);
			noti.setMsg("Item " + itemDto.getItemName() + " saved successfully!");
			this.noti = noti;

		} catch (

		Exception e) {
			LOG.error("Error occurred while saving the Item", e);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.ERROR);
			noti.setMsg(
					"An error occurred while saving the Item. Please contact the system administrator for further support.");
			this.noti = noti;
		}

		return "redirect:";
	}

	@GetMapping("/deleteItem")
	public String deleteItem(@RequestParam("id") Long id) {
		try {
			// Don't delete the item from the DB. This item may be used for rent and
			// reports. Hence, just mark the item as inactive.
			Item item = itemService.getById(id);
			item.setActive(false);
			itemService.save(item);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.SUCCESS);
			noti.setMsg("Item deleted successfully!");
			this.noti = noti;

			return "redirect:";

		} catch (Exception e) {
			LOG.error("Error occurred while deleting the Item", e);

			NotificationModel noti = new NotificationModel();
			noti.setType(NotiType.ERROR);
			noti.setMsg(
					"An error occurred while deleting the Item. Please contact the system administrator for further support.");
			this.noti = noti;

			return "redirect:";
		}
	}
}
