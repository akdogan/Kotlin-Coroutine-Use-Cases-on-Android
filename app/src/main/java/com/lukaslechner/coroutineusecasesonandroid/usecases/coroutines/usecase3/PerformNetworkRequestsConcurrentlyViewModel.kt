package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase3

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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
        viewModelScope.launch {
            uiState.postValue(UiState.Loading)

            val androidVersions = listOf(27, 28, 29)

            val jobList = mutableListOf<Deferred<VersionFeatures?>>()
            androidVersions.forEach { version ->
                jobList.add(
                    async {
                        // async holds the exception and passes until await is called on it
                        try {
                            mockApi.getAndroidVersionFeatures(version)
                        } catch (e: Exception) {
                            Timber.e("Call failed for version $version with ${e.message} ")
                            null
                        }
                    }
                )
            }

            val resultList = jobList.awaitAll().filterNotNull()

            val resultUiState = if (resultList.isEmpty()) {
                UiState.Error("All calls actually failed, sorry")
            } else {
                UiState.Success(resultList)
            }

            uiState.postValue(resultUiState)

        }
    }



    private suspend fun MutableList<VersionFeatures>.safeAddFeatureSet(version: Int) {
        try {
            this.add(mockApi.getAndroidVersionFeatures(version))
        } catch (e: Exception) {
            Timber.e("Failed to load Features for Api Level $version - ${e.message}")
        }
    }
}