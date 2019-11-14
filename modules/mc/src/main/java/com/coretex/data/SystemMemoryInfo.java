package com.coretex.data;

public class SystemMemoryInfo {

	private Long freeMemory;
	private Long maxMemory;
	private Long totalMemory;

	public SystemMemoryInfo() {

		this.freeMemory = Runtime.getRuntime().freeMemory();
		this.maxMemory = Runtime.getRuntime().maxMemory();

		this.totalMemory = Runtime.getRuntime().totalMemory();
	}


	public Long getFreeMemory() {
		return freeMemory;
	}

	public Long getMaxMemory() {
		return maxMemory;
	}

	public Long getTotalMemory() {
		return totalMemory;
	}
}
