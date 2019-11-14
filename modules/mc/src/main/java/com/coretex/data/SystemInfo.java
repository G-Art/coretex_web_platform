package com.coretex.data;

public class SystemInfo {
	private String nameOS;
	private String osType;
	private String osVersion;

	private String processorIdentifier;
	private String processorArchitecture;
	private String processorArchitew6432;
	private String numberOfProcessors;
	private Integer availableProcessors;

	private SystemMemoryInfo systemMemoryInfo;


	public SystemInfo() {
		this.nameOS = System.getProperty("os.name");
		this.osType = System.getProperty("os.arch");
		this.osVersion = System.getProperty("os.version");

		this.processorIdentifier = System.getenv("PROCESSOR_IDENTIFIER");
		this.processorArchitecture = System.getenv("PROCESSOR_ARCHITECTURE");
		this.processorArchitew6432 = System.getenv("PROCESSOR_ARCHITEW6432");
		this.numberOfProcessors = System.getenv("NUMBER_OF_PROCESSORS");

		this.availableProcessors = Runtime.getRuntime().availableProcessors();

		systemMemoryInfo = new SystemMemoryInfo();
	}

	public String getNameOS() {
		return nameOS;
	}

	public String getOsType() {
		return osType;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public String getProcessorIdentifier() {
		return processorIdentifier;
	}

	public String getProcessorArchitecture() {
		return processorArchitecture;
	}

	public String getProcessorArchitew6432() {
		return processorArchitew6432;
	}

	public String getNumberOfProcessors() {
		return numberOfProcessors;
	}

	public Integer getAvailableProcessors() {
		return availableProcessors;
	}

	public SystemMemoryInfo getSystemMemoryInfo() {
		return systemMemoryInfo;
	}
}
