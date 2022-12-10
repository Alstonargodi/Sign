package com.example.bisindo_recognitions.model.injection

import android.content.Context
import com.example.bisindo_recognitions.model.remote.BisindoApiConfig
import com.example.bisindo_recognitions.model.repository.DataRepository

object Injection {
    fun provideDataRepository(context: Context): DataRepository{
        return DataRepository(BisindoApiConfig.setBisindoApiService())
    }
}