package com.example.cote.domain.repo

import android.content.Context
import android.nfc.NfcAdapter
import com.example.cote.data.remote.CoteApi
import com.example.cote.data.remote.ReqCheckOtp
import com.example.cote.data.remote.ReqOtpNum
import com.example.cote.data.remote.ResCheckOtp
import com.example.cote.data.remote.ResOtp
import com.example.cote.data.remote.ResReadStudent
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
            val res = api.logincheck(ReqCheckOtp(number, code , "admin"))

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


    override fun checkNFCStatus(context: Context): String {


        val nfcAdapter : NfcAdapter? = NfcAdapter.getDefaultAdapter(context)

        if(nfcAdapter == null){
            return "NFCisUnAvilabel"
        }else{
            if(!nfcAdapter.isEnabled){
                return "NFCisOff"
            }else{
                return "NFCIsOn"

            }
        }
    }


    override suspend fun ReadStudent(token : String): Result<ResReadStudent?>  =
        runCatching {
            val res = api.ReadStudent(token)

            if (res.isSuccessful) {
                res.body()
            } else {

                val ebody  = res.errorBody()?.string()?.let {
                    Gson().fromJson(it, ResReadStudent::class.java)
                }

                ebody
            }
        }.onFailure {
            handleError(it)
        }


}
