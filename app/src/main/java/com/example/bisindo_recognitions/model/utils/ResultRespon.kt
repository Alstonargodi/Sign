package com.example.bisindo_recognitions.model.utils

sealed class ResultRespon <out R> private constructor(){
    data class Sucess<out T>(val data: T): ResultRespon<T>()
    data class Error(val error : String): ResultRespon<Nothing>()
    object Loading : ResultRespon<Nothing>()
}
