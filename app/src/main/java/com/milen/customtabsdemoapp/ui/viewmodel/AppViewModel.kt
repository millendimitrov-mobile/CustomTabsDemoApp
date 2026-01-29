package com.milen.customtabsdemoapp.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.milen.customtabsdemoapp.data.UrlStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(private val urlStore: UrlStore) : ViewModel() {

    val loginUrl: StateFlow<String> = urlStore.loginUrlFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UrlStore.DEFAULT_URL
        )

    private val _editingUrl = MutableStateFlow("")
    val editingUrl: StateFlow<String> = _editingUrl.asStateFlow()

    private val _urlValidationError = MutableStateFlow<UrlValidationError?>(null)
    val urlValidationError: StateFlow<UrlValidationError?> = _urlValidationError.asStateFlow()

    fun setEditingUrl(url: String) {
        _editingUrl.value = url
        _urlValidationError.value = validateUrl(url)
    }

    fun initEditingUrl(url: String) {
        _editingUrl.value = url
        _urlValidationError.value = null
    }

    fun saveUrl() {
        val url = _editingUrl.value
        if (validateUrl(url) == null) {
            viewModelScope.launch {
                urlStore.setLoginUrl(url)
            }
        }
    }

    fun resetToDefault() {
        viewModelScope.launch {
            urlStore.setLoginUrl(UrlStore.DEFAULT_URL)
        }
    }

    fun isUrlValid(): Boolean = validateUrl(_editingUrl.value) == null

    private fun validateUrl(url: String): UrlValidationError? {
        if (url.isBlank()) {
            return UrlValidationError.INVALID_FORMAT
        }

        if (!url.startsWith("https://")) {
            return UrlValidationError.NOT_HTTPS
        }

        val uri = try {
            Uri.parse(url)
        } catch (e: Exception) {
            return UrlValidationError.INVALID_FORMAT
        }

        if (uri.host.isNullOrBlank()) {
            return UrlValidationError.NO_HOST
        }

        return null
    }
}

enum class UrlValidationError {
    NOT_HTTPS,
    INVALID_FORMAT,
    NO_HOST
}
