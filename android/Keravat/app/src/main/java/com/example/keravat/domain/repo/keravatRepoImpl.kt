package com.example.keravat.domain.repo

import android.app.Activity
import android.app.Application
import android.content.Context
import android.nfc.NfcAdapter
import android.util.Log
import com.example.keravat.data.remote.KeravatApi
import com.example.keravat.data.remote.ReqAddDescription
import com.example.keravat.data.remote.ReqCheckDescription
import com.example.keravat.data.remote.ReqCheckOtp
import com.example.keravat.data.remote.ReqOtpNum
import com.example.keravat.data.remote.ResAddDescription
import com.example.keravat.data.remote.ResCheckDescription
import com.example.keravat.data.remote.ResCheckOtp
import com.example.keravat.data.remote.ResOtp
import com.example.keravat.data.remote.ResReadAttendance
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import retrofit2.HttpException

class KeravatRepoImpl(
    private val api: KeravatApi,
) : KeravatRepo {


    /**
     * Sends a login request using a phone number.
     */
    override suspend fun LoginWithNumber(number: String): Result<ResOtp?> =
        runCatching {
            val res = api.loginWithNumber(ReqOtpNum(number))

            if (res.isSuccessful) {
                res.body()
            } else {

                val ebody  = res.errorBody()?.string()?.let {
                    Gson().fromJson(it, ResOtp::class.java)
                }

                ebody
            }
        }.onFailure {
            handleError(it)
        }


    /**
     * Handles network and HTTP exceptions in API calls.
     */
    private fun handleError(e: Throwable) {
        if (e is HttpException) {
            e.response()?.errorBody()?.string()
        }
    }


    /**
     * Check the Otp Code with Server
     */

    override suspend fun LoginCheckOtp(number: String, code: String): Result<ResCheckOtp?> =
        runCatching {
            val res = api.logincheck(ReqCheckOtp(number, code))

            if (res.isSuccessful) {
                res.body()
            } else {

                val ebody  = res.errorBody()?.string()?.let {
                    Gson().fromJson(it, ResCheckOtp::class.java)
                }

                ebody
            }
        }.onFailure {
            handleError(it)
        }



    override suspend fun readAttendance(MainToken: String): Result<ResReadAttendance?> =

        runCatching {
            val response = api.readAttendance(MainToken)
            Log.d("APIRES" , response.body().toString())
            response.body()
        }.onFailure {e ->
            Log.e("TEST_API", "Error: ${e.message}")
            handleError(e)
        }


    override suspend fun addAttendanceDescription(
        token: String,
        text: String,
        date: String
    ): Result<ResAddDescription?> =

        runCatching {
            val response = api.addAttendanceDescription(token , ReqAddDescription(text , date))
            response.body()
        }.onFailure {e ->
            handleError(e)
        }







}
