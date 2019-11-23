package com.starbugs.wasalni_core.data.source

import android.content.Context
import android.content.SharedPreferences
import kotlin.reflect.KClass

class SharedPreferenceSource(context: Context) {
    companion object {
        private const val SHARED_PREFERENCES_NAME = "wasalniSharedPreferences"
        const val TOKEN_KEY = "token_key"
    }

    val sharedPreferences: SharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    inline fun <reified T> get(key: String) :T? {
        return when {
            T::class.java.isAssignableFrom(String::class.java) -> (sharedPreferences.getString(key, null) as T)
            T::class.java.isAssignableFrom(Boolean::class.java) -> (sharedPreferences.getBoolean(key, false) as T)
            T::class.java.isAssignableFrom(Float::class.java) -> (sharedPreferences.getFloat(key, 0f) as T)
            T::class.java.isAssignableFrom(Int::class.java) -> (sharedPreferences.getInt(key, 0) as T)
            T::class.java.isAssignableFrom(Long::class.java) -> (sharedPreferences.getLong(key, 0) as T)
            else -> null
        }
    }

    inline fun <reified T> set(key: String, value: T) {
         when {
            T::class.java.isAssignableFrom(String::class.java) -> sharedPreferences.edit().putString(key, value as String).apply()
            T::class.java.isAssignableFrom(Boolean::class.java) -> sharedPreferences.edit().putBoolean(key, value as Boolean).apply()
            T::class.java.isAssignableFrom(Float::class.java) -> sharedPreferences.edit().putFloat(key, value as Float).apply()
            T::class.java.isAssignableFrom(Int::class.java) ->sharedPreferences.edit().putInt(key, value as Int).apply()
            T::class.java.isAssignableFrom(Long::class.java) -> sharedPreferences.edit().putLong(key, value as Long).apply()
        }

    }

    fun clearsharedPreferences() {
        sharedPreferences
            .edit()
            .clear()
            .apply()
    }
}