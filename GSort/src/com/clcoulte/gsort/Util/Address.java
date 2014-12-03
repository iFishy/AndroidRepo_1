package com.clcoulte.gsort.Util;

import com.clcoulte.gsort.MailUtil.GMailRunner;

public class Address {

	String baseDomain, username;
	String[] subDomains;

	public Address() {
		new Address(GMailRunner.DEFAULT_LOGIN_UN);
	}

	public Address(String address) {
		// TODO expects valid address
		String[] split = address.split("@");
		username = split[0];
		split = split[1].split(".");
		subDomains = new String[split.length - 2];
		int i = 0;
		for (; i < split.length - 2; i++) {
			subDomains[i] = split[i];
		}
		baseDomain = split[i] + "." + split[i+1];
	}
}
