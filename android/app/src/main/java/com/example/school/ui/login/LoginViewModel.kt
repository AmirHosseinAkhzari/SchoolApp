package com.example.school.ui.login

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.school.data.model.NfcManager
import com.example.school.domain.repository.AstinRepo
import com.example.school.domain.repository.AstinRepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


data class LoginUidata (
    val guidText : String = "" ,
    val uid : String = "" ,
)


@HiltViewModel
class LoginViewModel@Inject constructor(
    private val repo : AstinRepo
) : ViewModel() {

    private  val _uiState  = MutableStateFlow(LoginUidata())
    val uiState : StateFlow<LoginUidata> = _uiState


    init {
        GuideLine()
    }


    fun GuideLine() {
        viewModelScope.launch {
            var dotCount = 0
            while (true) {
                delay(500)
                dotCount = (dotCount + 1) % 4
                _uiState.value = _uiState.value.copy(
                    guidText = ".".repeat(dotCount) + " گوشی را جا به جا کنید"
                )
            }
        }
    }



    fun startNfc(activity: Activity) {
        repo.readNfc(activity)
    }






}