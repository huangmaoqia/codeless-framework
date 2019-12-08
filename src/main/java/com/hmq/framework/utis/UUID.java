package com.hmq.framework.utis;

import java.io.Serializable;
import java.security.SecureRandom;

public final class UUID implements Serializable, Comparable<UUID> {
	private static final long serialVersionUID = -4856846361193249489L;
	private final long mostSigBits;
	private final long leastSigBits;
	private static volatile SecureRandom numberGenerator = null;

	private UUID(byte[] data) {
		long msb = 0L;
		long lsb = 0L;

		assert data.length == 16 : "data must be 16 bytes in length";

		int i;
		for (i = 0; i < 8; ++i) {
			msb = msb << 8 | (long) (data[i] & 255);
		}

		for (i = 8; i < 16; ++i) {
			lsb = lsb << 8 | (long) (data[i] & 255);
		}

		this.mostSigBits = msb;
		this.leastSigBits = lsb;
	}

	public static UUID randomUUID() {
		SecureRandom ng = numberGenerator;
		if (ng == null) {
			numberGenerator = ng = new SecureRandom();
		}

		byte[] randomBytes = new byte[16];
		ng.nextBytes(randomBytes);
		randomBytes[6] = (byte) (randomBytes[6] & 15);
		randomBytes[6] = (byte) (randomBytes[6] | 64);
		randomBytes[8] = (byte) (randomBytes[8] & 63);
		randomBytes[8] = (byte) (randomBytes[8] | 128);
		return new UUID(randomBytes);
	}

	public String toString() {
		return digits(this.mostSigBits >> 32, 8) + digits(this.mostSigBits >> 16, 4) + digits(this.mostSigBits, 4)
				+ digits(this.leastSigBits >> 48, 4) + digits(this.leastSigBits, 12);
	}

	private static String digits(long val, int digits) {
		long hi = 1L << digits * 4;
		return Long.toHexString(hi | val & hi - 1L).substring(1);
	}

	public int compareTo(UUID o) {
		return 0;
	}
}	
