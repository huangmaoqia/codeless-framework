package com.hmq.framework.utis;

public final class UUIDUtil{
	public static final String EMPTY_UUID = "00000000000000000000000000000000";

	public static String newUUID() {
		return UUID.randomUUID().toString();
	}
}
