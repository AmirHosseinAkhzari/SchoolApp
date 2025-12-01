package com.example.school.domain.repository

import android.app.Activity
import android.app.Application
import android.util.Log
import com.example.school.R
import com.example.school.data.model.NfcManager
import com.example.school.data.remote.AstinApi
import com.example.school.data.remote.ReqAddNotificationToken
import com.example.school.data.remote.ReqCheckOtp
import com.example.school.data.remote.ReqOtpNum
import com.example.school.data.remote.ReqOtpUid
import com.example.school.data.remote.ResAddNotificationToken
import com.example.school.data.remote.ResCheckOtp
import com.example.school.data.remote.ResOtp
import com.example.school.data.remote.ResReadAttendance
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import retrofit2.HttpException
import kotlin.onFailure

/**
 * Repository implementation that handles NFC reading and API login operations.
 */
class AstinRepoImpl(
    private val api: AstinApi,
    private val nfcManager: NfcManager,
    private val appContext: Application
) : AstinRepo {

    // SharedFlow used to emit new NFC tag UIDs
    private val _nfcData = MutableSharedFlow<String>(replay = 0, extraBufferCapacity = 1)
    override val nfcData: SharedFlow<String> = _nfcData

    // Holds the last detected NFC UID to prevent duplicate reads
    override var lastUid: String? = null

    /**
     * Starts reading NFC tags and emits the UID when a new tag is detected.
     */
    override fun readNfc(activity: Activity) {
        nfcManager.onTagRead = { uid ->
            if (uid != lastUid) {
                lastUid = uid
                _nfcData.tryEmit(uid)
            }
        }
        nfcManager.enableReader(activity)
    }

    /**
     * Stops NFC reading.
     */
    override fun stopReading(activity: Activity) {
        nfcManager.disableReader(activity)
    }

    /**
     * Sends a login request using a phone number.
     */
    override suspend fun LoginWithNumber(number: String): Result<ResOtp?> =
        runCatching { api.loginWithNumber(ReqOtpNum(number)).body() }
            .onFailure { e -> handleError(e) }

    /**
     * Sends a login request using an NFC UID.
     */
    override suspend fun LoginWithUid(uid: String): Result<ResOtp?> =
        runCatching { api.loginWithUid(ReqOtpUid(uid)).body() }
            .onFailure { e -> handleError(e) }

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
            val response = api.logincheck(ReqCheckOtp(number, code))
            response.body()
        }.onFailure { e ->
            Log.e("TEST_API", "Error: ${e.message}")
            handleError(e)
        }


    override suspend fun addNotificationToken(notificationToken: String, MainToken: String):
            Result<ResAddNotificationToken?> =
        runCatching {
            val response = api.addNotificationToken(ReqAddNotificationToken(notificationToken) , MainToken)
            response.body()
        }.onFailure {e ->
            Log.e("TEST_API", "Error: ${e.message}")
            handleError(e)
        }


    override suspend fun readAttendance(MainToken: String): Result<ResReadAttendance?> =

        runCatching {
            val response = api.readAttendance(MainToken)
            response.body()
        }.onFailure {e ->
            Log.e("TEST_API", "Error: ${e.message}")
            handleError(e)
        }



}

