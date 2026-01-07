package com.example.cote.ui.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cote.domain.repo.CoteRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
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
    val token : String = "" ,
    val number: String = "" ,
    val e : String = ""
)


/**
 * ViewModel for phone number login flow.
 */
@HiltViewModel
class LoginWithNumberViewModel @Inject constructor(
    private val repo: CoteRepo
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
                        number = num
                    )
                }else{
                    Log.d("api", res.toString())
                    _uiState.value = _uiState.value.copy(network = NetworkMode.Failure , e = res.getOrNull()!!.message)
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
    private val repo: CoteRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiData())
    val uiState: StateFlow<LoginUiData> = _uiState





    fun CheckOtp(number : String , code : String  , context: Context){

        Log.d("code" , code)
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
                    _uiState.value = _uiState.value.copy(network = NetworkMode.Failure , e = res.getOrNull()!!.message)
                }
            } else {
                _uiState.value = _uiState.value.copy(network = NetworkMode.Failure)
            }
        }

    }

    fun GetNumber(context: Context): String? {
        val sharedPref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val token = sharedPref.getString("number", null)

        return token
    }

}
