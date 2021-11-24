package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase4

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.launch

class VariableAmountOfNetworkRequestsViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequestsSequentially() {
        viewModelScope.launch {
            uiState.postValue(UiState.Loading)

            val versionList = try {
                mockApi.getRecentAndroidVersions()
            } catch (e: Exception){
                emptyList()
            }


        }
    }

    fun performNetworkRequestsConcurrently() {

    }
}