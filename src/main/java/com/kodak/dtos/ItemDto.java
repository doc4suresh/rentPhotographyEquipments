package com.kodak.dtos;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDto {

	private Long id;
	private String itemCode;
	private String itemName;
	private String keyFeatures;
	private String overview;
	private int qty;
	private double rentPrice1To2Days;
	private double rentPrice3To4Days;
	private double rentPrice5Days;
	private double deposit1To2Days;
	private double deposit3To4Days;
	private double deposit5Days;
	private int available;
	private boolean active = true;
	private String category;
	private String brand;
	private MultipartFile itemImg;

	// Item3dObjectDetection
	private Long item3dObjectDetectionId;
	private String operatingRange;

	// ItemBattery
	private Long itemBatteryId;
	private String chemistry;
	private String capacity;
	private String configuration;
	private String batteryOperatingTemperature;

	// ItemCameraSpecs
	private Long itemCameraSpecsId;
	private String sensor;
	private String effectivePixels;
	private String lensFieldofView;
	private String aperture;
	private String minimumFocusingDistance;
	private String photoISORange;
	private String electronicShutterSpeed;
	private String photoFormat;
	private String videoFormat;

	// ItemCharger
	private Long itemChargerId;
	private String acInputPower;
	private String outputVoltage;
	private String powerRating;

	// ItemExposureControl
	private Long itemExposureControlId;
	private String exposureControl;
	private String exposureFlashModes;
	private String isoSensitivity;
	private String meteringMethod;
	private String exposureModes;
	private String exposureCompensation;
	private String meteringRange;
	private String whiteBalance;
	private String selfTimer;
	private String shutterSpeed;
	private String continuousShooting;

	// ItemFlash
	private Long itemFlashId;
	private String builtInFlash;
	private String dedicatedFlashSystem;
	private String externalFlashConnection;
	private String flashModes;
	private String maximumSyncSpeed;
	private String flashCompensation;

	// ItemFlightControlSystem
	private Long itemFlightControlSystemId;
	private String gnssSupport;

	// ItemFocus
	private Long itemFocusId;
	private String focusType;
	private String focusMode;
	private String autofocusPoints;
	private String autofocusSensitivity;

	// ItemGeneralSpecs
	private Long itemGeneralSpecsId;
	private String numberOfRotors;
	private String operatingTemperature;
	private String overallDimensions;

	// ItemImaging
	private Long itemImagingId;
	private String lensMount;
	private String cameraFormat;
	private String pixels;
	private String maximumResolution;
	private String aspectRatio;
	private String sensorType;
	private String sensorSize;
	private String imageFileFormat;
	private String bitDepth;
	private String imageStabilization;

	// ItemInterface
	private Long itemInterfaceId;
	private String memoryCardSlot;
	private String interfaceConnectivity;
	private String wireless;

	// ItemPackagingInfo
	private Long itemPackagingInfoId;
	private String packageWeight;
	private String dimensions;

	// ItemPerformance
	private Long itemPerformanceId;
	private String maximumTakeoffWeight;
	private String maximumHorizontalSpeed;
	private String maximumAscentSpeed;
	private String maximumDescentSpeed;
	private String maximumWindResistance;
	private String flightCeiling;
	private String maximumFlightTime;
	private String maximumHoverTime;
	private String maximumTiltAngle;
	private String hoveringAccuracy;

	// ItemRemoteControllerOrTransmitter
	private Long itemRemoteControllerOrTransmitterId;
	private String operatingFrequency;
	private String maximumOperatingDistance;
	private String transmitterPower;
	private String connectivity;

	// ItemVideoSpecs
	private Long itemVideoSpecsId;
	private String recordingModes;
	private String externalRecordingModes;
	private String recordingLimit;
	private String videoEncoding;
	private String audioRecording;
	private String audioFileFormat;

	// ItemViewfinderMonitor
	private Long itemViewfinderMonitorId;
	private String viewfinderType;
	private String viewfinderSize;
	private String viewfinderResolution;
	private String viewfinderEyePoint;
	private String viewfinderCoverage;
	private String viewfinderMagnification;
	private String diopterAdjustment;
	private String monitorSize;
	private String monitorResolution;
	private String monitorType;

	// ItemVisionSystem
	private Long itemVisionSystemId;
	private String visionSystem;
	private String obstacleSensoryRange;
	private String forwardFieldOfView;
	private String downwardFieldOfView;
	private String backwardFieldOfView;
	private String operatingEnvironment;
}
