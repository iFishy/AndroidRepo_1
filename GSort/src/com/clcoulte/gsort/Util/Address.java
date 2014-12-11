package com.clcoulte.gsort.Util;

import com.clcoulte.gsort.MailUtil.GMailRunner;

public class Address {

	private String baseDomain, username;
	private String[] subDomains;
	private int count;

	public Address() {
		this(GMailRunner.DEFAULT_LOGIN_UN, 1);
	}

	public Address(String address, int count) {
		// TODO expects valid address
		String[] split = address.split("@");
		username = split[0];
		split = split[1].split("\\.");
		subDomains = new String[split.length - 2];
		int i = 0;
		for (; i < split.length - 2; i++) {
			subDomains[i] = split[i];
		}
		baseDomain = split[i] + "." + split[i + 1];
		this.count = count;
	}

	public String getBaseDomain() {
		return baseDomain;
	}

	public String getUsername() {
		return username;
	}

	public String[] getSubDomains() {
		return subDomains;
	}

	public int getCount() {

		return count;
	}
}
