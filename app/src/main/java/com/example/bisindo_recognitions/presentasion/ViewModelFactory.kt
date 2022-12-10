package com.example.bisindo_recognitions.presentasion

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bisindo_recognitions.model.injection.Injection
import com.example.bisindo_recognitions.model.repository.DataRepository
import com.example.bisindo_recognitions.presentasion.home.HomeFragmentViewModel

class ViewModelFactory private constructor(
    private val repository: DataRepository
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(HomeFragmentViewModel::class.java)->{
                HomeFragmentViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object{
        @Volatile
        private var instance : ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this){
                instance ?: ViewModelFactory(
                   Injection.provideDataRepository(context)
                )
            }.also { instance = it }
    }
}