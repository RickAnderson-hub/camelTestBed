package org.example.camel.util;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class UniqueId {

	/*
	 * The range of long is -9223372036854775808 to 9223372036854775807, and we use the lower third of the positive range
	 * giving us 6.148914691×10¹⁸ unique ids before we start to repeat.  That is 6,148,914,691,000,000,000 unique ids or
	 *  six quintillion one hundred forty-eight quadrillion nine hundred fourteen trillion six hundred ninety-one billion unique ids.
	 * @see https://www.calculatorsoup.com/calculators/math/scientific-notation-converter.php
	 */
	private static final AtomicLong counter = new AtomicLong(Math.abs(new Random().nextLong() / 3));

	static void main(String[] args) {
		System.out.println(generate());
	}

	public static long generate() {
		return counter.incrementAndGet();
	}
}