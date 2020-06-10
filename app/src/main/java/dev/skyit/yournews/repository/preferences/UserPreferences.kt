package dev.skyit.yournews.repository.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dev.skyit.yournews.api.models.CategoryFilter
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

interface IUserPreferences {
    var preferredCountry: String

    var preferredCategories: List<CategoryFilter>

}

interface ISharedPreferences {
    val pref: SharedPreferences
}

class UserPreferences(
    private val context: Context
): IUserPreferences, ISharedPreferences {

    override val pref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    override var preferredCountry: String by PreferenceString("preferredCountry", "ro")

    @OptIn(ImplicitReflectionSerializer::class)
    override var preferredCategories: List<CategoryFilter> by PreferenceProperty(
        key = "preferredCategories",
        defaultValue = emptyList(),
        converter = object : CustomStringConvertible<List<CategoryFilter>> {
            override fun fromString(str: String): List<CategoryFilter>? {
                return str.split("|").map {
                    Json.parse(CategoryFilter::class.serializer(), it)
                }
            }

            override fun toString(data: List<CategoryFilter>): String {
                return data.joinToString(separator = "|", transform = {
                    Json.stringify(CategoryFilter::class.serializer(), it)
                })
            }
        }
    )



}