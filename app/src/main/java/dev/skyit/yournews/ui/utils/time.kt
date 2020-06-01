package dev.skyit.yournews.ui.utils

import com.soywiz.klock.DateTime
import com.soywiz.klock.days
import com.soywiz.klock.hours
import com.soywiz.klock.weeks
import dev.skyit.yournews.ui.present
import kotlin.math.roundToInt

fun DateTime.relativeTime() : String {
    val diff = present - this
    if (diff < 1.hours) {
        return "${diff.minutes.roundToInt()} minutes ago"
    }
    if (diff < 1.days) {
        return "${diff.hours.roundToInt()} hours ago"
    }

    if (diff < 2.days) {
        return "Yesterday"
    }

    if (diff < 1.weeks) {
        return "${diff.days.roundToInt()} days ago"
    }

    if (diff < 5.weeks) {
        return "${diff.weeks.roundToInt()} weeks ago"
    }

    return "${(diff.weeks / 5).roundToInt()} months ago"
}