package com.installment.manager.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferencesManager(private val context: Context) {

    companion object {
        val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
        val NOTIFICATIONS_ENABLED_KEY = booleanPreferencesKey("notifications_enabled")
        val REMINDER_DAYS_KEY = intPreferencesKey("reminder_days")
        val CURRENCY_UNIT_KEY = stringPreferencesKey("currency_unit") // "toman" or "rial"
    }

    val isDarkMode: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[DARK_MODE_KEY] ?: false }

    val isNotificationsEnabled: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[NOTIFICATIONS_ENABLED_KEY] ?: true }

    val reminderDays: Flow<Int> = context.dataStore.data
        .map { prefs -> prefs[REMINDER_DAYS_KEY] ?: 3 }

    val currencyUnit: Flow<String> = context.dataStore.data
        .map { prefs -> prefs[CURRENCY_UNIT_KEY] ?: "toman" }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[DARK_MODE_KEY] = enabled
        }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[NOTIFICATIONS_ENABLED_KEY] = enabled
        }
    }

    suspend fun setReminderDays(days: Int) {
        context.dataStore.edit { prefs ->
            prefs[REMINDER_DAYS_KEY] = days
        }
    }

    suspend fun setCurrencyUnit(unit: String) {
        context.dataStore.edit { prefs ->
            prefs[CURRENCY_UNIT_KEY] = unit
        }
    }
}
