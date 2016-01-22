package com.sauloborges.ysura.util;

/**
 * This class represents a Licence Plate Generator in the pattern 'AAA-000'
 * 
 * copied in http://stackoverflow.com/questions/26818478/generating-random-license-plate}
 * 
 * @author sauloborges
 *
 */
public class LicencePlateGenerator {

	private static String generateLetters(int amount) {
		String letters = "";
		int n = 'Z' - 'A' + 1;
		for (int i = 0; i < amount; i++) {
			char c = (char) ('A' + Math.random() * n);
			letters += c;
		}
		return letters;
	}

	private static String generateDigits(int amount) {
		String digits = "";
		int n = '9' - '0' + 1;
		for (int i = 0; i < amount; i++) {
			char c = (char) ('0' + Math.random() * n);
			digits += c;
		}
		return digits;
	}

	public static final String generateLicencePlate() {
		String licensePlate;

		String letters = generateLetters(3);
		String digits = generateDigits(3);

		licensePlate = letters + "-" + digits;
		return licensePlate;
	}
}
