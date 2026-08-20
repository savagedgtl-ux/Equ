package com.equ.app.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.landingPageDataStore by preferencesDataStore(name = "landing_page")

data class LandingPageConfig(
    val displayName: String = "",
    val bio: String = "",
    val services: String = "",
)

/**
 * Draft of the therapist's landing page. Editing/saving the draft is fully
 * local; actually publishing it to a hosted URL needs the backend from
 * ARCHITECTURE.md and is stubbed as "coming soon" in the UI.
 */
class LandingPagePrefs(private val context: Context) {
    private val nameKey = stringPreferencesKey("display_name")
    private val bioKey = stringPreferencesKey("bio")
    private val servicesKey = stringPreferencesKey("services")

    val config: Flow<LandingPageConfig> = context.landingPageDataStore.data.map { prefs ->
        LandingPageConfig(
            displayName = prefs[nameKey] ?: "",
            bio = prefs[bioKey] ?: "",
            services = prefs[servicesKey] ?: "",
        )
    }

    suspend fun save(config: LandingPageConfig) {
        context.landingPageDataStore.edit { prefs ->
            prefs[nameKey] = config.displayName
            prefs[bioKey] = config.bio
            prefs[servicesKey] = config.services
        }
    }
}
