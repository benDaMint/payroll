package com.lawencon.payroll.util;

import java.util.Random;

public class GenerateUtil {
	
	public static String generateCode() {
		final byte leftLimit = 48;
		final byte rightLimit = 90;
		final byte invoiceLength = 5;
		final Random rand = new Random();
		
		final String invoice = rand.ints(leftLimit, rightLimit + 1).filter(i -> (i <= 57 || i >= 65)).limit(invoiceLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
		return invoice;
	}
	
	public static int generateNumber(int index) {
		Random rand = new Random();
		return rand.nextInt(index);
	}
}
