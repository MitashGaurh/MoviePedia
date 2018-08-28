package com.mitash.moviepedia.util

import android.text.TextUtils
import timber.log.Timber
import java.text.DateFormatSymbols
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Date utils
 */
class DateUtil
/**
 * Don't let anyone instantiate this class.
 */
private constructor() {

    init {
        throw Error("Do not need instantiate!")
    }

    companion object {
        private const val TAG = "DateUtil"
        const val yyyyMMDD = "yyyy-MM-dd"
        const val yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss"
        const val HHmmss = "HH:mm:ss"
        const val hhmmss = "hh:mm:ss a"
        const val DB_DATA_FORMAT = "yyyy-MM-DD HH:mm:ss"

        private val format = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        private val yearFormat = SimpleDateFormat(yyyyMMDD, Locale.ENGLISH)
        private val formatDateTime = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale
                .ENGLISH)

        fun longToSimpleDateString(DateTimeLong: Long): String {
            val parsedDateString: String
            val parsedDate = Date(DateTimeLong)
            parsedDateString = format.format(parsedDate)
            return parsedDateString
        }

        fun longToSimpleDateTimeString(DateTimeLong: Long): String {
            val parsedDateString: String
            val parsedDate = Date(DateTimeLong)
            parsedDateString = formatDateTime.format(parsedDate)
            return parsedDateString
        }

        fun stringToDateTime(dateString: String): Date? {
            var date: Date? = null
            formatDateTime.timeZone = TimeZone.getDefault()
            formatDateTime.isLenient = false
            try {
                date = formatDateTime.parse(dateString)
            } catch (parseException: ParseException) {
                Timber.tag(TAG).e("Exception while parsing " + dateString + "  to " +
                        "DateTime !!", parseException)
            }

            return date
        }


        fun stringToDate(dateString: String): Date? {
            var date: Date? = null
            format.timeZone = TimeZone.getDefault()
            format.isLenient = false
            try {
                date = format.parse(dateString)
            } catch (parseException: ParseException) {
                Timber.tag(TAG).e("Exception while parsing " + dateString + " to Date " +
                        "!! ", parseException)
            }

            return date
        }

        fun splittedDate(date: String): IntArray {
            val splitted = IntArray(3)
            val dateSpilt = date.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            for (i in dateSpilt.indices) {
                splitted[i] = Integer.parseInt(dateSpilt[i])
            }

            return splitted
        }

        fun getTimeString(minute: Int, hour: Int): String {
            val time = StringBuilder()
            if (hour < 10) {
                time.append("0")
            }
            time.append(hour)
            time.append(":")
            if (minute < 10) {
                time.append("0")
            }
            time.append(minute)
            time.append(":00")
            return time.toString()
        }

        fun getDateString(day: Int, month: Int, year: Int): String {
            val stringBuilder = StringBuilder()
            if (day < 10) {
                stringBuilder.append("0")
            }
            stringBuilder.append(day)
            stringBuilder.append("-")
            if (month < 10) {
                stringBuilder.append("0")
            }
            stringBuilder.append(month)
            stringBuilder.append("-")

            stringBuilder.append(year)

            return stringBuilder.toString()
        }

        fun getMonth(i: Int): String {
            return DateFormatSymbols().shortMonths[i - 1]
        }


        fun isThisDateValid(dateToValidate: String): Boolean {
            var isValid: Boolean = try {
                format.parse(dateToValidate)
                true
            } catch (parseException: ParseException) {
                false
            }
            format.timeZone = TimeZone.getDefault()
            format.isLenient = false

            return isValid
        }


        private fun getCalendar(date: Date): Calendar {
            val cal = Calendar.getInstance(Locale.ENGLISH)
            cal.time = date
            return cal
        }

        fun nextDayOfWeek(dow: Int): Calendar {
            val date = Calendar.getInstance()
            date.set(Calendar.HOUR_OF_DAY, 10)
            date.set(Calendar.MINUTE, 0)
            var diff = dow - date.get(Calendar.DAY_OF_WEEK)
            if (diff < 0) {
                diff += 7
            }
            date.add(Calendar.DAY_OF_MONTH, diff)
            return date
        }

        @Throws(Exception::class)
        fun dateToString(date: Date, pattern: String): String {
            return SimpleDateFormat(pattern, Locale.getDefault()).format(date)
        }

        @Throws(Exception::class)
        fun stringToDate(dateStr: String, pattern: String): Date {
            return SimpleDateFormat(pattern, Locale.getDefault()).parse(dateStr)
        }

        fun formatDate(date: Date, type: String): String? {
            try {
                val df = SimpleDateFormat(type, Locale.getDefault())
                return df.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }

        fun parseDate(dateStr: String, type: String): Date? {
            val df = SimpleDateFormat(type, Locale.getDefault())
            var date: Date? = null
            try {
                date = df.parse(dateStr)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return date

        }

        fun getYear(date: Date): Int {
            val c = Calendar.getInstance()
            c.time = date
            return c.get(Calendar.YEAR)
        }

        fun getMonth(date: Date): Int {
            val c = Calendar.getInstance()
            c.time = date
            return c.get(Calendar.MONTH) + 1

        }

        fun getDay(date: Date): Int {
            val c = Calendar.getInstance()
            c.time = date
            return c.get(Calendar.DAY_OF_MONTH)
        }

        fun nextYearDate(i: Int): Date {
            val cal = Calendar.getInstance()
            cal.add(Calendar.YEAR, 3) // to get previous year add -1
            return cal.time
        }

        fun getPreviousYear(i: Int): Int {
            val prevYear = Calendar.getInstance()
            prevYear.add(Calendar.YEAR, i)
            return prevYear.get(Calendar.YEAR)
        }

        fun getPreviousYearDate(i: Int): String {
            val cal = Calendar.getInstance()
            if (i != 0) {
                cal.add(Calendar.YEAR, i)
            }

            return yearFormat.format(cal.time)
        }

        fun getPreviousMonthDate(i: Int): String {
            val cal = Calendar.getInstance()
            if (i != 0) {
                cal.add(Calendar.MONTH, i)
            }

            return yearFormat.format(cal.time)
        }

        private fun translateDate(time: Long, curTime: Long): String? {
            val oneDay = (24 * 60 * 60).toLong()
            val today = Calendar.getInstance()
            today.timeInMillis = curTime * 1000
            today.set(Calendar.HOUR_OF_DAY, 0)
            today.set(Calendar.MINUTE, 0)
            today.set(Calendar.SECOND, 0)
            val todayStartTime = today.timeInMillis / 1000
            if (time >= todayStartTime) {
                val d = curTime - time
                if (d <= 60) {
                    return "1 minute ago"
                } else if (d <= 60 * 60) {
                    var m = d / 60
                    if (m <= 0) {
                        m = 1
                    }
                    return m.toString() + " minutes ago"
                } else {
                    val dateFormat = SimpleDateFormat("today HH:mm", Locale.getDefault())
                    val date = Date(time * 1000)
                    var dateStr = dateFormat.format(date)
                    if (!TextUtils.isEmpty(dateStr) && dateStr.contains(" 0")) {
                        dateStr = dateStr.replace(" 0", " ")
                    }
                    return dateStr
                }
            } else {
                if (time < todayStartTime && time > todayStartTime - oneDay) {
                    val dateFormat = SimpleDateFormat("yesterday HH:mm", Locale.getDefault())
                    val date = Date(time * 1000)
                    var dateStr = dateFormat.format(date)
                    if (!TextUtils.isEmpty(dateStr) && dateStr.contains(" 0")) {

                        dateStr = dateStr.replace(" 0", " ")
                    }
                    return dateStr
                } else if (time < todayStartTime - oneDay && time > todayStartTime - 2 * oneDay) {
                    val dateFormat = SimpleDateFormat("day before yesterday HH:mm", Locale.getDefault())
                    val date = Date(time * 1000)
                    var dateStr = dateFormat.format(date)
                    if (!TextUtils.isEmpty(dateStr) && dateStr.contains(" 0")) {
                        dateStr = dateStr.replace(" 0", " ")
                    }
                    return dateStr
                } else {
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                    val date = Date(time * 1000)
                    var dateStr = dateFormat.format(date)
                    if (!TextUtils.isEmpty(dateStr) && dateStr.contains(" 0")) {
                        dateStr = dateStr.replace(" 0", " ")
                    }
                    return dateStr
                }
            }
        }
    }
}
