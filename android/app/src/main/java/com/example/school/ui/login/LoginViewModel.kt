package com.example.school.ui.login

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.school.domain.repository.AstinRepo
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/** Represents different network request states. */
enum class NetworkMode { Loading, Success, Failure, None }

/** Holds all UI data related to login operations. */
data class LoginUiData(
    val guidText: String = "",
    val uid: String = "a",
    val network: NetworkMode = NetworkMode.None,
    val otpText: String = "" ,
    val token : String = ""
)

/**
 * ViewModel for NFC-based login (using sleeve).
 */
@HiltViewModel
class LoginWithSleeveViewModel @Inject constructor(
    private val repo: AstinRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiData())
    val uiState: StateFlow<LoginUiData> = _uiState

    init {
        startGuideAnimation()
        observeNfcData()
    }

    /** Resets login data to default. */
    fun resetData() {
        _uiState.value = _uiState.value.copy(uid = "", network = NetworkMode.None)
    }

    /** Shows a looping guide animation (text dots). */
    private fun startGuideAnimation() {
        viewModelScope.launch {
            var dot = 0
            while (true) {
                delay(500)
                dot = (dot + 1) % 4
                _uiState.value = _uiState.value.copy(
                    guidText = ".".repeat(dot) + " گوشی خود را جا به جا کنید "
                )
            }
        }
    }

    /** Stops NFC reading and clears last UID. */
    fun stopNfc(activity: Activity) {
        repo.stopReading(activity)
        repo.lastUid = null
    }

    /** Starts NFC tag detection. */
    fun startNfc(activity: Activity) {
        repo.readNfc(activity)
    }

    /** Observes incoming NFC data and triggers login automatically. */
    private fun observeNfcData() {
        viewModelScope.launch {
            repo.nfcData.collect { uid ->
                _uiState.value = _uiState.value.copy(uid = uid, network = NetworkMode.Loading)
                Log.d("nfc" , uid)
                val res = repo.LoginWithUid(uid)
                if (res.isSuccess) {
                    if(res.getOrNull()?.code == 200) {
                        _uiState.value = _uiState.value.copy(
                            network = NetworkMode.Success,
                        )
                    }else{
                        _uiState.value = _uiState.value.copy(network = NetworkMode.Failure)
                    }
                } else {
                    _uiState.value = _uiState.value.copy(network = NetworkMode.Failure)
                }
            }
        }
    }
}

/**
 * ViewModel for phone number login flow.
 */
@HiltViewModel
class LoginWithNumberViewModel @Inject constructor(
    private val repo: AstinRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiData())
    val uiState: StateFlow<LoginUiData> = _uiState

    /** Resets network state. */
    fun resetNetwork() {
        _uiState.value = _uiState.value.copy(network = NetworkMode.None)
    }

    /** Sends login request via phone number. */
    fun loginWithNumber(num: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(network = NetworkMode.Loading)
            val res = repo.LoginWithNumber(num)
            if (res.isSuccess) {
                if(res.getOrNull()?.code == 200) {
                    _uiState.value = _uiState.value.copy(
                        network = NetworkMode.Success,
                    )
                }else{
                    _uiState.value = _uiState.value.copy(network = NetworkMode.Failure)
                }
            } else {
                _uiState.value = _uiState.value.copy(network = NetworkMode.Failure)
            }
        }
    }
}

/**
 * ViewModel for OTP (One-Time Password) verification screen.
 */
@HiltViewModel
class LoginOtpViewModel @Inject constructor(
    private val repo: AstinRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiData())
    val uiState: StateFlow<LoginUiData> = _uiState




    fun CheckOtp(number : String , code : String  , context: Context){

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(network = NetworkMode.Loading)

            val res =  repo.LoginCheckOtp(number, code)
            if (res.isSuccess) {
                if(res.getOrNull()?.code == 200) {
                    val body = res.getOrNull()
                    if (body?.token != null){
                        val sharedPref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
                        val editor = sharedPref.edit()
                        editor.putString("token" , body?.token)
                        editor.apply()
                    }


                    _uiState.value = _uiState.value.copy(
                        network = NetworkMode.Success,
                        token = body?.token ?: ""
                    )
                }else{
                    _uiState.value = _uiState.value.copy(network = NetworkMode.Failure)
                }
            } else {
                _uiState.value = _uiState.value.copy(network = NetworkMode.Failure)
            }
        }

    }

}
