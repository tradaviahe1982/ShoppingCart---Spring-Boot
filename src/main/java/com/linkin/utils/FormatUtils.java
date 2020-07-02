package com.linkin.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class FormatUtils {
	public static String formatCurrency(long number) {
		NumberFormat format = NumberFormat.getInstance(new Locale("vi"));
		format.setMaximumFractionDigits(0);

		return format.format(number);
	}

	public static void main(String[] args) {
		System.out.println(FormatUtils.formatCurrency((long)(35000 * 0.15)));
	}
	
	
}
