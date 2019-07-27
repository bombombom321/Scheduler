package com.simplemobiletools.calendar.pro.models

import androidx.collection.LongSparseArray
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.simplemobiletools.calendar.pro.extensions.seconds
import com.simplemobiletools.calendar.pro.helpers.*
import com.simplemobiletools.commons.extensions.addBitIf
import org.joda.time.DateTime
import org.json.JSONArray
import org.json.JSONException
import java.io.Serializable
import android.text.TextUtils
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.simplemobiletools.calendar.pro.helpers.Formatter
import java.util.*
import kotlin.collections.ArrayList


@SerializedName("repetition_exceptions") var re :String="";

@Entity(tableName = "events", indices = [(Index(value = ["id"], unique = true))])
data class Event(
        @PrimaryKey(autoGenerate = true) var id: Long?,
        @SerializedName("start_ts")    @ColumnInfo(name = "start_ts") var startTS: Long = 0L,
        @SerializedName("end_ts") @ColumnInfo(name = "end_ts") var endTS: Long = 0L,
        @SerializedName("title") @ColumnInfo(name = "title") var title: String = "",
        @SerializedName("location") @ColumnInfo(name = "location") var location: String = "",
        @SerializedName("description") @ColumnInfo(name = "description") var description: String = "",
        @SerializedName("reminder_1_minutes") @ColumnInfo(name = "reminder_1_minutes") var reminder1Minutes: Int = -1,
        @SerializedName("reminder_2_minutes") @ColumnInfo(name = "reminder_2_minutes") var reminder2Minutes: Int = -1,
        @SerializedName("reminder_3_minutes") @ColumnInfo(name = "reminder_3_minutes") var reminder3Minutes: Int = -1,
        @SerializedName("reminder_1_type") @ColumnInfo(name = "reminder_1_type") var reminder1Type: Int = REMINDER_NOTIFICATION,
        @SerializedName("reminder_2_type") @ColumnInfo(name = "reminder_2_type") var reminder2Type: Int = REMINDER_NOTIFICATION,
        @SerializedName("reminder_3_type") @ColumnInfo(name = "reminder_3_type") var reminder3Type: Int = REMINDER_NOTIFICATION,
        @SerializedName("repeat_interval") @ColumnInfo(name = "repeat_interval") var repeatInterval: Int = 0,
        @SerializedName("repeat_rule") @ColumnInfo(name = "repeat_rule") var repeatRule: Int = 0,
        @SerializedName("repeat_limit") @ColumnInfo(name = "repeat_limit") var repeatLimit: Long = 0L,
      @ColumnInfo(name = "repetition_exceptions") var repetitionExceptions: ArrayList<String> = ArrayList(),
        @SerializedName("attendees") @ColumnInfo(name = "attendees") var attendees: String = "",
        @SerializedName("import_id") @ColumnInfo(name = "import_id") var importId: String = "",
        @SerializedName("flags") @ColumnInfo(name = "flags") var flags: Int = 0,
        @SerializedName("event_type") @ColumnInfo(name = "event_type") var eventType: Long = REGULAR_EVENT_TYPE_ID,
        @SerializedName("parent_id") @ColumnInfo(name = "parent_id") var parentId: Long = 0,
        @SerializedName("last_updated") @ColumnInfo(name = "last_updated") var lastUpdated: Long = 0L,
        @SerializedName("source") @ColumnInfo(name = "source") var source: String = SOURCE_SIMPLE_CALENDAR)
    : Serializable {

init {
    if(importId.equals("")) importId = UUID.randomUUID().toString().replace("-", "") + System.currentTimeMillis().toString()
}


    companion object {
        private const val serialVersionUID = -32456795132345616L
    }


    fun addIntervalTime(original: Event) {
        val oldStart = Formatter.getDateTimeFromTS(startTS)
        val newStart: DateTime
        newStart = when (repeatInterval) {
            DAY -> oldStart.plusDays(1)
            else -> {
                when {
                    repeatInterval % YEAR == 0 -> when (repeatRule) {
                        REPEAT_ORDER_WEEKDAY -> addXthDayInterval(oldStart, original, false)
                        REPEAT_ORDER_WEEKDAY_USE_LAST -> addXthDayInterval(oldStart, original, true)
                        else -> oldStart.plusYears(repeatInterval / YEAR)
                    }
                    repeatInterval % MONTH == 0 -> when (repeatRule) {
                        REPEAT_SAME_DAY -> addMonthsWithSameDay(oldStart, original)
                        REPEAT_ORDER_WEEKDAY -> addXthDayInterval(oldStart, original, false)
                        REPEAT_ORDER_WEEKDAY_USE_LAST -> addXthDayInterval(oldStart, original, true)
                        else -> oldStart.plusMonths(repeatInterval / MONTH).dayOfMonth().withMaximumValue()
                    }
                    repeatInterval % WEEK == 0 -> {
                        // step through weekly repetition by days too, as events can trigger multiple times a week
                        oldStart.plusDays(1)
                    }
                    else -> oldStart.plusSeconds(repeatInterval)
                }
            }
        }

        val newStartTS = newStart.seconds()
        val newEndTS = newStartTS + (endTS - startTS)
        startTS = newStartTS
        endTS = newEndTS
    }

    // if an event should happen on 31st with Same Day monthly repetition, dont show it at all at months with 30 or less days
    private fun addMonthsWithSameDay(currStart: DateTime, original: Event): DateTime {
        var newDateTime = currStart.plusMonths(repeatInterval / MONTH)
        if (newDateTime.dayOfMonth == currStart.dayOfMonth) {
            return newDateTime
        }

        while (newDateTime.dayOfMonth().maximumValue < Formatter.getDateTimeFromTS(original.startTS).dayOfMonth().maximumValue) {
            newDateTime = newDateTime.plusMonths(repeatInterval / MONTH)
            newDateTime = newDateTime.withDayOfMonth(currStart.dayOfMonth)
        }
        return newDateTime
    }

    // handle monthly repetitions like Third Monday
    private fun addXthDayInterval(currStart: DateTime, original: Event, forceLastWeekday: Boolean): DateTime {
        val day = currStart.dayOfWeek
        var order = (currStart.dayOfMonth - 1) / 7
        val properMonth = currStart.withDayOfMonth(7).plusMonths(repeatInterval / MONTH).withDayOfWeek(day)
        var firstProperDay = properMonth.dayOfMonth % 7
        if (firstProperDay == 0)
            firstProperDay = properMonth.dayOfMonth

        // check if it should be for example Fourth Monday, or Last Monday
        if (forceLastWeekday && (order == 3 || order == 4)) {
            val originalDateTime = Formatter.getDateTimeFromTS(original.startTS)
            val isLastWeekday = originalDateTime.monthOfYear != originalDateTime.plusDays(7).monthOfYear
            if (isLastWeekday)
                order = -1
        }

        val daysCnt = properMonth.dayOfMonth().maximumValue
        var wantedDay = firstProperDay + order * 7
        if (wantedDay > daysCnt)
            wantedDay -= 7

        if (order == -1) {
            wantedDay = firstProperDay + ((daysCnt - firstProperDay) / 7) * 7
        }

        return properMonth.withDayOfMonth(wantedDay)
    }

    fun getIsAllDay() = flags and FLAG_ALL_DAY != 0

    fun getReminders() = setOf(
            Reminder(reminder1Minutes, reminder1Type),
            Reminder(reminder2Minutes, reminder2Type),
            Reminder(reminder3Minutes, reminder3Type)
    ).filter { it.minutes != REMINDER_OFF }

    // properly return the start time of all-day events as midnight
    fun getEventStartTS(): Long {
        return if (getIsAllDay()) {
            Formatter.getDateTimeFromTS(startTS).withTime(0, 0, 0, 0).seconds()
        } else {
            startTS
        }
    }

    fun getCalDAVEventId(): Long {
        return try {
            (importId.split("-").lastOrNull() ?: "0").toString().toLong()
        } catch (e: NumberFormatException) {
            0L
        }
    }

    fun getCalDAVCalendarId() = if (source.startsWith(CALDAV)) (source.split("-").lastOrNull() ?: "0").toString().toInt() else 0

    // check if its the proper week, for events repeating every x weeks
    // get the week number since 1970, not just in the current year
    fun isOnProperWeek(startTimes: LongSparseArray<Long>): Boolean {
        val initialWeekNumber = Formatter.getDateTimeFromTS(startTimes[id!!]!!).millis / (7 * 24 * 60 * 60 * 1000)
        val currentWeekNumber = Formatter.getDateTimeFromTS(startTS).millis / (7 * 24 * 60 * 60 * 1000)
        return (initialWeekNumber - currentWeekNumber) % (repeatInterval / WEEK) == 0L
    }

    fun updateIsPastEvent() {
        val endTSToCheck = if (startTS < getNowSeconds() && getIsAllDay()) {
            Formatter.getDayEndTS(Formatter.getDayCodeFromTS(endTS))
        } else {
            endTS
        }
        isPastEvent = endTSToCheck < getNowSeconds()
    }

    fun addRepetitionException(daycode: String) {
        var newRepetitionExceptions = repetitionExceptions
        newRepetitionExceptions.add(daycode)
        newRepetitionExceptions = newRepetitionExceptions.distinct().toMutableList() as ArrayList<String>
        repetitionExceptions = newRepetitionExceptions
    }

    var isPastEvent: Boolean
        get() = flags and FLAG_IS_PAST_EVENT != 0
        set(isPastEvent) {
            flags = flags.addBitIf(isPastEvent, FLAG_IS_PAST_EVENT)
        }

    var color: Int = 0





}
