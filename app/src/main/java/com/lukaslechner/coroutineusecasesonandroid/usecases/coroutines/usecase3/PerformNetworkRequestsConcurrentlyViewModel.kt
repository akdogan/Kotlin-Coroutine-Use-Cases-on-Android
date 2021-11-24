package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase3

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.AndroidVersion
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class PerformNetworkRequestsConcurrentlyViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequestsSequentially() {
        uiState.value = UiState.Loading

        viewModelScope.launch {
            val androidVersions = listOf(27, 28, 29)

            val listOfFeatures: MutableList<VersionFeatures> = mutableListOf()
            androidVersions.forEach {
                listOfFeatures.safeAddFeatureSet(it)
            }

            uiState.postValue(
                if (listOfFeatures.isNotEmpty()) {
                    UiState.Success(listOfFeatures)
                } else {
                    UiState.Error("Failed loading Dude!")
                }
            )
        }
    }

    fun performNetworkRequestsConcurrently() {

    }

    private suspend fun MutableList<VersionFeatures>.safeAddFeatureSet(version: Int) {
        try {
            this.add(mockApi.getAndroidVersionFeatures(version))
        } catch (e: Exception) {
            Timber.e("Failed to load Features for Api Level $version - ${e.message}")
        }
    }
}