package com.andrew.photoweatherapp.presentation.features.cameraScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

sealed class CameraState
object IdleState : CameraState()
object LoadingState : CameraState()
object CapturedState : CameraState()
data class Saved(val uri:String?) : CameraState()

class CameraViewModel(
    val stateLiveData: MutableLiveData<CameraState> = MutableLiveData()
) : ViewModel() {
    var temporaryUriString = ""

    init {
        stateLiveData.value = IdleState
    }

    fun setCaptured(uri: String?) = uri
        .takeUnless { it.isNullOrBlank() }
        ?.let {
            temporaryUriString = it
            stateLiveData.value = CapturedState
        }

    fun cancelPhoto() {
        stateLiveData.value = IdleState
        temporaryUriString = ""
    }

    fun setLoading(){
        stateLiveData.value = LoadingState
    }

    fun setSaved(uri: String?){
        stateLiveData.value = Saved(uri)
    }
}