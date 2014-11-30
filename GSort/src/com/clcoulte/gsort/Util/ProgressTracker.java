package com.clcoulte.gsort.Util;

public class ProgressTracker {

	private static final int DEF_MAX = 100, DEF_MIN = 0, DEF_CURRENT = DEF_MIN;

	private int max, min, current;

	public ProgressTracker() {
		max = DEF_MAX;
		min = DEF_MIN;
		current = DEF_CURRENT;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

}
