package com.equ.app.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.equ.app.data.local.LandingPageConfig
import com.equ.app.data.local.LandingPagePrefs
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LandingPageViewModel(private val prefs: LandingPagePrefs) : ViewModel() {

    val config: StateFlow<LandingPageConfig> = prefs.config
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), LandingPageConfig())

    fun save(config: LandingPageConfig) {
        viewModelScope.launch { prefs.save(config) }
    }

    class Factory(private val prefs: LandingPagePrefs) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            LandingPageViewModel(prefs) as T
    }
}
