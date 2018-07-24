package com.miao.test;

import org.joda.time.LocalDate;


public class DayCountConvention_ACT_ACT_AFB {

	public double getDaycountFraction(LocalDate startDate, LocalDate endDate) {
		if(startDate.isAfter(endDate)) {
			return -getDaycountFraction(endDate,startDate);
		}

		/*
		 * Find the "fractionalPeriodEnd", i.e. subtract whole years from endDate.
		 */
		LocalDate fractionalPeriodEnd = endDate.plusYears(startDate.getYear() - endDate.getYear());
		MockJava8LocalDate myFractionalPeriodEnd = new MockJava8LocalDate(fractionalPeriodEnd);
		MockJava8LocalDate myEndDate = new MockJava8LocalDate(endDate);
		MockJava8LocalDate mystartDate = new MockJava8LocalDate(startDate);

		// preserving 'end-of-month' if endDate is 28/Feb of non-leap-year or 29/Feb of non-leap-year.
		if (endDate.getDayOfMonth() == myEndDate.lengthOfMonth()) {
			fractionalPeriodEnd = fractionalPeriodEnd.withDayOfMonth(myFractionalPeriodEnd.lengthOfMonth());
		}

		if (fractionalPeriodEnd.isBefore(startDate)) {
			fractionalPeriodEnd.plusYears(1);
			// preserving 'end-of-month' if endDate is 28/Feb of non-leap-year or 29/Feb of non-leap-year, again after changing the years.
			if (endDate.getDayOfMonth() == myEndDate.lengthOfMonth()) {
				fractionalPeriodEnd = fractionalPeriodEnd.withDayOfMonth(myFractionalPeriodEnd.lengthOfMonth());
			}
		}

		double daycountFraction = endDate.getYear() - fractionalPeriodEnd.getYear();

		double fractionPeriodDenominator = 365.0;
		if(myFractionalPeriodEnd.isLeapYear()) {
			LocalDate feb29th = MockJava8LocalDate.of(fractionalPeriodEnd.getYear(), 2, 29);
			if(startDate.compareTo(feb29th) <= 0 && fractionalPeriodEnd.compareTo(feb29th) > 0) {
				fractionPeriodDenominator = 366.0;
			}
		}
		else if(mystartDate.isLeapYear()) {
			LocalDate feb29th = MockJava8LocalDate.of(fractionalPeriodEnd.getYear(), 2, 29);
			if(startDate.compareTo(feb29th) <= 0 && fractionalPeriodEnd.compareTo(feb29th) > 0) {
				fractionPeriodDenominator = 366.0;
			}
		}

		daycountFraction += getDaycount(startDate, fractionalPeriodEnd) / fractionPeriodDenominator;

		return daycountFraction;
	}

	public double getDaycount(LocalDate startDate, LocalDate endDate) {
		if(startDate.isAfter(endDate)) {
			return -getDaycount(endDate,startDate);
		}

		return daysBetween(startDate, endDate);
	}


	public static double daysBetween(LocalDate startDate, LocalDate endDate) {
		MockJava8LocalDate myEndDate = new MockJava8LocalDate(endDate);
		MockJava8LocalDate mystartDate = new MockJava8LocalDate(startDate);
		return (myEndDate.toEpochDay() - mystartDate.toEpochDay());
	}
}