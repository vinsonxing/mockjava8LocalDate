package com.miao.test;

import org.joda.time.LocalDate;

public class MockJava8LocalDate {
	private static final int DAYS_PER_CYCLE = 146097;
	static final long DAYS_0000_TO_1970 = (DAYS_PER_CYCLE * 5L) - (30L * 365L + 7L);
	LocalDate localDate;
	public MockJava8LocalDate(LocalDate l) {
		this.localDate = l;
	}

	public LocalDate getLocalDate() {
		return localDate;
	}

	public boolean isLeapYear() {
		long prolepticYear = localDate.getYear();
		return ((prolepticYear & 3) == 0) && ((prolepticYear % 100) != 0 || (prolepticYear % 400) == 0);
	}

	public int lengthOfMonth() {
		int month = localDate.getMonthOfYear();
		switch (month) {
            case 2:
                return (isLeapYear() ? 29 : 28);
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            default:
                return 31;
        }
	}

	public static LocalDate of(int year,
            int monthOfYear,
            int dayOfMonth) {
		return new LocalDate(year, monthOfYear, dayOfMonth);
	}

	public long toEpochDay() {
        long y = localDate.getYear();
        long m = localDate.getMonthOfYear();
        long total = 0;
        total += 365 * y;
        if (y >= 0) {
            total += (y + 3) / 4 - (y + 99) / 100 + (y + 399) / 400;
        } else {
            total -= y / -4 - y / -100 + y / -400;
        }
        total += ((367 * m - 362) / 12);
        total += localDate.getDayOfMonth() - 1;
        if (m > 2) {
            total--;
            if (isLeapYear() == false) {
                total--;
            }
        }
        return total - DAYS_0000_TO_1970;
    }

}