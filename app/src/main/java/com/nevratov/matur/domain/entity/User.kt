package com.nevratov.matur.domain.entity

import android.icu.util.Calendar
import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@Immutable
@Parcelize
data class User(
    val id: Int,
    val name: String,
    val logoUrl: String?,
    val gender: String,
    val birthday: String,
    val wasOnlineTimestamp: Long,
    val cityId: Int,
    val aboutMe: String?,
    val height: Int,
    val weight: Int,
    val bodyType: String,
    val education: String,
    val job: String?,
    val maritalStatus: String,
    val children: String,
    val house: String,
    val nationality: String,
    val languageSkills: String,
    val religion: String,
    val religiosityLevel: String,
    val expectations: String?,
    val drinking: String,
    val smoking: String,
    val isBlocked: Boolean
) : Parcelable {

    private val currentTime: Long
        get() = System.currentTimeMillis()

    private val difference: Long
        get() = currentTime - (wasOnlineTimestamp)

    private val calendar: Calendar
        get() = Calendar.getInstance().apply { time = Date(wasOnlineTimestamp) }

    private val minute: Int
        get() = calendar.get(Calendar.MINUTE)

    private val hour: Int
        get() = calendar.get(Calendar.HOUR_OF_DAY)

    private val day: Int
        get() = calendar.get(Calendar.DAY_OF_MONTH)

    private val month: Int
        get() = calendar.get(Calendar.MONTH)

    private val year: Int
        get() = calendar.get(Calendar.YEAR)

    private val pattern: String
        get() = "Был(а) в сети"

    val wasOnlineText: String
        get() = when (difference) {
            in 0..MILLIS_IN_MIN -> {
                "$pattern только что"
            }

            in MILLIS_IN_MIN..MILLIS_IN_HOUR -> {
                "$pattern ${TimeUnit.MILLISECONDS.toMinutes(difference)} мин. назад"
            }

            in MILLIS_IN_HOUR..MILLIS_IN_6_HOUR -> {
                "$pattern ${TimeUnit.MILLISECONDS.toHours(difference)} ч. назад"
            }

            in MILLIS_IN_6_HOUR..MILLIS_IN_DAY -> {
                String.format(Locale.getDefault(), "$pattern в %02d:%02d", hour, minute)
            }

            in MILLIS_IN_DAY..MILLIS_IN_WEEK -> {
                String.format(
                    locale = Locale.getDefault(),
                    "$pattern %02d.%02d в %02d:%02d",
                    day,
                    month,
                    hour,
                    minute
                )
            }

            in MILLIS_IN_WEEK..MILLIS_IN_MONTH -> {
                "$pattern ${TimeUnit.MILLISECONDS.toDays(difference)} дн. назад"
            }

            else -> {
                String.format(Locale.getDefault(), "$pattern %02d.%02d.$year", day, month)
            }
        }

    private companion object {
        private const val MILLIS_IN_MIN = 60_000
        private const val MILLIS_IN_HOUR = 3_600_000
        private const val MILLIS_IN_6_HOUR = 21_600_000
        private const val MILLIS_IN_DAY = 86_400_000
        private const val MILLIS_IN_WEEK = 604_800_000
        private const val MILLIS_IN_MONTH = 2_592_000_000
    }
}
