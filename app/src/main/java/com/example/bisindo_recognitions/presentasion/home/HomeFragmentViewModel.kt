package com.example.bisindo_recognitions.presentasion.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.bisindo_recognitions.model.remote.recognitionresponse.BisindoApiResponse
import com.example.bisindo_recognitions.model.repository.DataRepository
import com.example.bisindo_recognitions.model.utils.ResultRespon

class HomeFragmentViewModel(
    private val repository: DataRepository
): ViewModel() {

    suspend fun getBisindoData(): LiveData<ResultRespon<BisindoApiResponse>> =
        repository.getBisindoData()
}