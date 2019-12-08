package com.hmq.framework.utis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {
	private final static String ymdPatternStr = "(\\d{4})\\D?(\\d{0,2})\\D?(\\d{0,2})";
	private final static Pattern ymdPattern = Pattern.compile(ymdPatternStr);

	private final static String hmsPatternStr = "(\\d{0,2})\\D?(\\d{0,2})\\D?(\\d{0,2})";
	private final static Pattern hmsPattern = Pattern.compile(hmsPatternStr);

	public static void main(String[] args) {
	}

	public static Date toDate(String dateStr) {
		Date d = null;
		String[] strArr = dateStr.split(" ");
		String ymdStr = strArr[0];
		Matcher ymdMatcher = ymdPattern.matcher(ymdStr);

		StringBuilder yyyyMMddHHmmss = new StringBuilder();
		if (ymdMatcher.find()) {
			yyyyMMddHHmmss.append(ymdMatcher.group(1));
			for (int i = 2; i <= 3; i++) {
				String str = ymdMatcher.group(i);
				if (str.length() == 0) {
					yyyyMMddHHmmss.append("01");
				} else if (str.length() == 1) {
					yyyyMMddHHmmss.append("0" + str);
				} else {
					yyyyMMddHHmmss.append(str);
				}
			}
			if (strArr.length >= 2) {
				String hmsStr = strArr[1];
				Matcher hmsMatcher = hmsPattern.matcher(hmsStr);
				if (hmsMatcher.find()) {
					for (int i = 1; i <= 3; i++) {
						String str = hmsMatcher.group(i);
						if (str.length() == 0) {
							yyyyMMddHHmmss.append("00");
						} else if (str.length() == 1) {
							yyyyMMddHHmmss.append("0" + str);
						} else {
							yyyyMMddHHmmss.append(str);
						}
					}
				}
			} else {
				yyyyMMddHHmmss.append("000000");
			}
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
			try {
				d = sf.parse(yyyyMMddHHmmss.toString());
			} catch (ParseException e) {
				throw new RuntimeException("传入格式错误");
			}
		}
		return d;
	}
}
