package com.example.bisindo_recognitions.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.bisindo_recognitions.model.remote.BisindoApiService
import com.example.bisindo_recognitions.model.remote.recognitionresponse.BisindoApiResponse
import com.example.bisindo_recognitions.model.utils.ResultRespon

class DataRepository(
    private val apiService: BisindoApiService
) {
    suspend fun getBisindoData(): LiveData<ResultRespon<BisindoApiResponse>> =
    liveData {
        emit(ResultRespon.Loading)
        try{
            emit(ResultRespon.Sucess(
                apiService.getBisindoSign()
            ))
        }catch (e : Exception){
            emit(ResultRespon.Error(e.message.toString()))
        }
    }
}
