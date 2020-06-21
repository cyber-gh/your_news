package dev.skyit.yournews.repository.preferences

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface CustomStringConvertible<T> {
    fun fromString(str: String) : T?
    fun toString(data: T) : String
}

class PreferenceString(
    private val key: String,
    private val defaultValue: String = ""
) : ReadWriteProperty<ISharedPreferences, String>{
    override fun getValue(thisRef: ISharedPreferences, property: KProperty<*>): String {
        return thisRef.pref.getString(key, defaultValue) ?: defaultValue
    }

    override fun setValue(thisRef: ISharedPreferences, property: KProperty<*>, value: String) {
        thisRef.pref.edit().putString(key, value).apply()
    }

}

class PreferenceBool(
    private val key: String,
    private val defaultValue: Boolean = false
) : ReadWriteProperty<ISharedPreferences, Boolean>{
    override fun getValue(thisRef: ISharedPreferences, property: KProperty<*>): Boolean {
        return thisRef.pref.getBoolean(key, defaultValue) ?: defaultValue
    }

    override fun setValue(thisRef: ISharedPreferences, property: KProperty<*>, value: Boolean) {
        thisRef.pref.edit().putBoolean(key, value).apply()
    }

}

class PreferenceProperty<T>(
    private val key: String,
    private val defaultValue: T,
    private val converter: CustomStringConvertible<T>
) : ReadWriteProperty<ISharedPreferences, T> {
    override fun getValue(thisRef: ISharedPreferences, property: KProperty<*>): T {
        return converter.fromString(thisRef.pref.getString(key, "")!!) ?: defaultValue
    }

    override fun setValue(thisRef: ISharedPreferences, property: KProperty<*>, value: T) {
        thisRef.pref.edit().putString(key, converter.toString(value)).apply()
    }

}