package com.milen.customtabsdemoapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "url_settings")

class UrlStore(private val context: Context) {

    private val loginUrlKey = stringPreferencesKey("login_url")

    val loginUrlFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[loginUrlKey] ?: DEFAULT_URL
    }

    suspend fun setLoginUrl(url: String) {
        context.dataStore.edit { preferences ->
            preferences[loginUrlKey] = url
        }
    }

    companion object {
        const val DEFAULT_URL = "https://697bc73c87d6a92e477ae7f9--velvety-malasada-9996dd.netlify.app/"
    }
}
