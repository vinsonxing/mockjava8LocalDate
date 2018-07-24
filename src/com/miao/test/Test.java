package com.miao.test;

import java.util.Calendar;

public class Test {

	public static void main(String[] args) {
		DayCountConvention_ACT_ACT_AFB afb = new DayCountConvention_ACT_ACT_AFB();
		System.out.println(afb.getDaycountFraction(MockJava8LocalDate.of(2018, 7, 19), MockJava8LocalDate.of(2020, 9, 30)));
		System.out.println(price(afb.getDaycountFraction(MockJava8LocalDate.of(2018, 7, 19), MockJava8LocalDate.of(2020, 9, 30)), 0.0698d, 0.0835d, 1d, 1));
	}

	public static double price(double timeToMaturity, double coupon, double yield, double redemption, int frequency) {
		double price = 0.0;

		if (timeToMaturity > 0) {
			price += redemption;
		}

		double paymentTime = timeToMaturity;
		while (paymentTime > 0) {
			price += coupon / frequency;

			// Discount back
			price = price / (1.0 + yield / frequency);
			paymentTime -= 1.0 / frequency;
		}

		// Accrue running period
		double accrualPeriod = 0.0 - paymentTime; // amount of running period
													// which lies in the past
													// (before settlement)
		price *= Math.pow(1.0 + yield / frequency, accrualPeriod * frequency);
		price -= coupon / frequency * accrualPeriod * frequency;
		return price * 100;
	}

	public static double price(java.util.Date settlementDate, java.util.Date maturityDate, double coupon, double yield,
			double redemption, int frequency) {
		double price = 0.0;

		if (maturityDate.after(settlementDate)) {
			price += redemption;
		}

		Calendar paymentDate = Calendar.getInstance();
		paymentDate.setTime(maturityDate);
		while (paymentDate.after(settlementDate)) {
			price += coupon;

			// Disocunt back
			price /= 1.0 + yield / frequency;
			paymentDate.add(Calendar.MONTH, -12 / frequency);
		}

		Calendar periodEndDate = (Calendar) paymentDate.clone();
		periodEndDate.add(Calendar.MONTH, +12 / frequency);

		// Accrue running period
		double accrualPeriod = (paymentDate.getTimeInMillis() - settlementDate.getTime())
				/ (periodEndDate.getTimeInMillis() - paymentDate.getTimeInMillis());

		price *= Math.pow(1.0 + yield / frequency, accrualPeriod);
		price -= coupon * accrualPeriod;

		return price * 100;
	}

}
