package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.launch
import timber.log.Timber

class Perform2SequentialNetworkRequestsViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun perform2SequentialNetworkRequest() {
        uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val latestVersion = mockApi.getRecentAndroidVersions().maxOf { it.apiLevel }
                val features = mockApi.getAndroidVersionFeatures(latestVersion)
                uiState.postValue(UiState.Success(features))
            } catch (e: Exception) {
                uiState.postValue((UiState.Error(e.message.toString())))
            }
        }
    }
}