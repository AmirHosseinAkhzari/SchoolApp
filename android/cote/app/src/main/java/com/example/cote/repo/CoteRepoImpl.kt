package com.example.cote.domain.repo

import com.example.cote.data.remote.CoteApi
import com.example.cote.data.remote.ReqCheckOtp
import com.example.cote.data.remote.ReqOtpNum
import com.example.cote.data.remote.ResCheckOtp
import com.example.cote.data.remote.ResOtp
import com.example.cote.domain.repo.CoteRepo
import com.google.gson.Gson
import retrofit2.HttpException

class CoteRepoImpl(
    private val api: CoteApi,
) : CoteRepo {


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




}
