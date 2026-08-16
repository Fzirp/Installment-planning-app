package com.installment.manager.util

import java.util.Calendar
import java.util.GregorianCalendar

/**
 * Simple Persian (Jalali/Shamsi) calendar converter.
 * No external dependency needed.
 */
object PersianDateUtil {

    private val persianMonthNames = arrayOf(
        "فروردین", "اردیبهشت", "خرداد",
        "تیر", "مرداد", "شهریور",
        "مهر", "آبان", "آذر",
        "دی", "بهمن", "اسفند"
    )

    private val persianDayNames = arrayOf(
        "شنبه", "یکشنبه", "دوشنبه", "سه‌شنبه", "چهارشنبه", "پنجشنبه", "جمعه"
    )

    private val persianDigits = charArrayOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')

    data class PersianDate(
        val year: Int,
        val month: Int,  // 1-12
        val day: Int     // 1-31
    ) {
        val monthName: String get() = persianMonthNames[month - 1]

        fun format(): String {
            return "${toPersianDigits(day)} $monthName ${toPersianDigits(year)}"
        }

        fun formatShort(): String {
            return "${toPersianDigits(year)}/${toPersianDigits(month.toString().padStart(2, '0'))}/${toPersianDigits(day.toString().padStart(2, '0'))}"
        }
    }

    fun toPersianDigits(number: Int): String = toPersianDigits(number.toString())

    fun toPersianDigits(str: String): String {
        val sb = StringBuilder()
        for (c in str) {
            if (c in '0'..'9') {
                sb.append(persianDigits[c - '0'])
            } else {
                sb.append(c)
            }
        }
        return sb.toString()
    }

    fun formatCurrency(amount: Long): String {
        val formatted = String.format("%,d", amount)
        return "${toPersianDigits(formatted)} تومان"
    }

    fun formatCurrencyRial(amount: Long): String {
        val formatted = String.format("%,d", amount)
        return "${toPersianDigits(formatted)} ریال"
    }

    fun gregorianToPersian(gy: Int, gm: Int, gd: Int): PersianDate {
        val gDaysInMonth = intArrayOf(0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334)
        var gy2 = if (gm > 2) gy + 1 else gy
        var days = 355666 + (365 * gy) + ((gy2 + 3) / 4) - ((gy2 + 99) / 100) + ((gy2 + 399) / 400) + gd + gDaysInMonth[gm - 1]
        var jy = -1595 + (33 * (days / 12053))
        days %= 12053
        jy += 4 * (days / 1461)
        days %= 1461
        if (days > 365) {
            jy += ((days - 1) / 365)
            days = (days - 1) % 365
        }
        val jm: Int
        val jd: Int
        if (days < 186) {
            jm = 1 + (days / 31)
            jd = 1 + (days % 31)
        } else {
            jm = 7 + ((days - 186) / 30)
            jd = 1 + ((days - 186) % 30)
        }
        return PersianDate(jy, jm, jd)
    }

    fun persianToGregorian(jy: Int, jm: Int, jd: Int): Triple<Int, Int, Int> {
        var jy2 = jy - 979
        var jm2 = jm - 1
        var jd2 = jd - 1
        var jDayNo = 365 * jy2 + (jy2 / 33) * 8 + (jy2 % 33 + 3) / 4
        for (i in 0 until jm2) {
            jDayNo += if (i < 6) 31 else 30
        }
        jDayNo += jd2

        var gDayNo = jDayNo + 79

        var gy = 1600 + 400 * (gDayNo / 146097)
        gDayNo %= 146097

        var leap = true
        if (gDayNo >= 36525) {
            gDayNo--
            gy += 100 * (gDayNo / 36524)
            gDayNo %= 36524
            if (gDayNo >= 365) gDayNo++ else leap = false
        }

        gy += 4 * (gDayNo / 1461)
        gDayNo %= 1461

        if (gDayNo >= 366) {
            leap = false
            gDayNo--
            gy += gDayNo / 365
            gDayNo %= 365
        }

        val gDaysInMonth = intArrayOf(31, if (leap) 29 else 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        var gm = 0
        while (gm < 12 && gDayNo >= gDaysInMonth[gm]) {
            gDayNo -= gDaysInMonth[gm]
            gm++
        }

        return Triple(gy, gm + 1, gDayNo + 1)
    }

    fun fromMillis(millis: Long): PersianDate {
        val cal = Calendar.getInstance()
        cal.timeInMillis = millis
        return gregorianToPersian(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) + 1,
            cal.get(Calendar.DAY_OF_MONTH)
        )
    }

    fun toMillis(jy: Int, jm: Int, jd: Int): Long {
        val (gy, gm, gd) = persianToGregorian(jy, jm, jd)
        val cal = Calendar.getInstance()
        cal.set(gy, gm - 1, gd, 0, 0, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    fun today(): PersianDate {
        return fromMillis(System.currentTimeMillis())
    }

    fun getMonthName(month: Int): String = persianMonthNames[month - 1]

    fun getDaysInMonth(year: Int, month: Int): Int {
        return when {
            month <= 6 -> 31
            month <= 11 -> 30
            else -> if (isLeapYear(year)) 30 else 29
        }
    }

    fun isLeapYear(year: Int): Boolean {
        val breaks = intArrayOf(
            1, 5, 9, 13, 17, 22, 26, 30
        )
        val mod = year % 33
        return mod in breaks
    }

    fun getDayOfWeek(millis: Long): Int {
        val cal = Calendar.getInstance()
        cal.timeInMillis = millis
        // Convert to Saturday-based week (Saturday=0, Friday=6)
        val dow = cal.get(Calendar.DAY_OF_WEEK)
        return when (dow) {
            Calendar.SATURDAY -> 0
            Calendar.SUNDAY -> 1
            Calendar.MONDAY -> 2
            Calendar.TUESDAY -> 3
            Calendar.WEDNESDAY -> 4
            Calendar.THURSDAY -> 5
            Calendar.FRIDAY -> 6
            else -> 0
        }
    }

    fun getDayOfWeekName(millis: Long): String {
        return persianDayNames[getDayOfWeek(millis)]
    }

    /**
     * Add months to a Persian date, returning the millis of the new date.
     */
    fun addMonths(startMillis: Long, months: Int): Long {
        val pd = fromMillis(startMillis)
        var newMonth = pd.month + months
        var newYear = pd.year

        while (newMonth > 12) {
            newMonth -= 12
            newYear++
        }
        while (newMonth < 1) {
            newMonth += 12
            newYear--
        }

        val maxDay = getDaysInMonth(newYear, newMonth)
        val newDay = minOf(pd.day, maxDay)

        return toMillis(newYear, newMonth, newDay)
    }
}
