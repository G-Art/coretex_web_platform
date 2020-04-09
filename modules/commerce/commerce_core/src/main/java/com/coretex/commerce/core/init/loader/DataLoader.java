package com.coretex.commerce.core.init.loader;

public interface DataLoader {
	public static final int PRIORITY_MIN = 0;
	public static final int PRIORITY_10 = 10;
	public static final int PRIORITY_20 = 20;
	public static final int PRIORITY_30 = 30;
	public static final int PRIORITY_40 = 40;
	public static final int PRIORITY_50 = 50;
	public static final int PRIORITY_60 = 60;
	public static final int PRIORITY_70 = 70;
	public static final int PRIORITY_80 = 80;
	public static final int PRIORITY_90 = 90;
	public static final int PRIORITY_MAX = 100;

	int priority();
	default boolean parallel(){
		return false;
	}
	boolean load(String name);
}
