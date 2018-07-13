package com.hapramp.utils;

public class ReputationCalc {
		public static double calculateReputation(long raw) {
				return (Math.log10(raw) - 9) * 9 + 25;
		}
}
