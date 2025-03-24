package com.kostov.freetodolist.presentation.screens.kitten

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kostov.freetodolist.data.remote.CatApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class KittenViewModel(
    private val api: CatApiService
) : ViewModel() {

    private val _catUrl = MutableStateFlow<String?>(null)
    val catUrl: StateFlow<String?> = _catUrl

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadRandomCat() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = api.getRandomCat()
                _catUrl.value = response.firstOrNull()?.url
            } catch (e: Exception) {
                _error.value = "Failed to load cat 😿"
            } finally {
                _isLoading.value = false
            }
        }
    }
}