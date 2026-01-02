package com.example.cote.domain.repo

import android.app.Activity
import android.content.Context
import android.nfc.NfcAdapter
import android.nfc.tech.MifareClassic
import com.example.cote.data.remote.CoteApi
import com.example.cote.data.remote.ReqAddAstin
import com.example.cote.data.remote.ReqCheckOtp
import com.example.cote.data.remote.ReqOtpNum
import com.example.cote.data.remote.ResAddAstin
import com.example.cote.data.remote.ResCheckOtp
import com.example.cote.data.remote.ResOtp
import com.example.cote.data.remote.ResReadStudent
import com.example.cote.domain.repo.CoteRepo
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.HttpException
import kotlin.coroutines.resume

class CoteRepoImpl(
    private val api: CoteApi
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


    override suspend fun AddAstin(token: String, data: ReqAddAstin): Result<ResAddAstin?> =
        runCatching {
            val res = api.AddAstin(token , data)

            if (res.isSuccessful) {
                res.body()
            } else {

                val ebody  = res.errorBody()?.string()?.let {
                    Gson().fromJson(it, ResAddAstin::class.java)
                }

                ebody
            }
        }.onFailure {
            handleError(it)
        }


//    override suspend fun readNfcTag(context: Context): String? = suspendCancellableCoroutine { cont ->
//
//        val nfcAdapter = NfcAdapter.getDefaultAdapter(context)
//        if (nfcAdapter == null) {
//            cont.resume(null) // Device doesn't support NFC
//            return@suspendCancellableCoroutine
//        }
//
//        val activity = context as? Activity
//        if (activity == null) {
//            cont.resume(null) // Context is not an Activity
//            return@suspendCancellableCoroutine
//        }
//
//        val callback = NfcAdapter.ReaderCallback { tag ->
//            val uid = tag.id.joinToString("") { "%02X".format(it) }
//
//
//            val convertedUid = uid
//                .chunked(2)
//                .reversed()
//                .joinToString("")
//                .toLong(16)
//                .toString()
//                .padStart(10, '0')
//
//            cont.resume(convertedUid)
//        }
//
//        // Enable reader mode
//        nfcAdapter.enableReaderMode(
//            activity,
//            callback,
//            NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_NFC_B or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
//            null
//        )
//
//        // If coroutine is cancelled, disable reader mode
//        cont.invokeOnCancellation {
//
//        }
//    }

    override suspend fun readNfcTag(context: Context): String? =
        suspendCancellableCoroutine { cont ->

            val activity = context as? Activity
                ?: return@suspendCancellableCoroutine cont.resume(null, null)

            val adapter = NfcAdapter.getDefaultAdapter(activity)
                ?: return@suspendCancellableCoroutine cont.resume(null, null)

            val callback = NfcAdapter.ReaderCallback { tag ->
                try {
                    val uid = tag.id.joinToString("") { "%02X".format(it) }

                    val mfc = MifareClassic.get(tag)
                    mfc.connect()

                    val sector = 1
                    val trailerBlock = mfc.sectorToBlock(sector) + 3

                    val ok = mfc.authenticateSectorWithKeyA(
                        sector,
                        MifareClassic.KEY_DEFAULT
                    )
                    if (!ok) {
                        mfc.close()
                        cont.resume(uid, null)
                        return@ReaderCallback
                    }

                    val lockedTrailer = byteArrayOf(

                        0xFF.toByte(),0xFF.toByte(),0xFF.toByte(),
                        0xFF.toByte(),0xFF.toByte(),0xFF.toByte(),


                        0x7F, 0x07, 0x88.toByte(), 0x69,


                        0xFF.toByte(),0xFF.toByte(),0xFF.toByte(),
                        0xFF.toByte(),0xFF.toByte(),0xFF.toByte()
                    )

                    mfc.writeBlock(trailerBlock, lockedTrailer)

                    mfc.close()

                    activity.runOnUiThread {
                        cont.resume(uid, null)
                    }

                } catch (e: Exception) {
                    cont.resume(null, null)
                }
            }

            adapter.enableReaderMode(
                activity,
                callback,
                NfcAdapter.FLAG_READER_NFC_A or
                        NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
                null
            )

            cont.invokeOnCancellation {
            }
        }

}
