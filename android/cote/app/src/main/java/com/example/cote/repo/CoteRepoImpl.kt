package com.example.cote.domain.repo

import android.app.Activity
import android.content.Context
import android.nfc.NfcAdapter
import android.nfc.tech.MifareClassic
import android.util.Log
import com.example.cote.data.remote.CoteApi
import com.example.cote.data.remote.ReqAddAdmin
import com.example.cote.data.remote.ReqAddAstin
import com.example.cote.data.remote.ReqAddStudent
import com.example.cote.data.remote.ReqAttendanceChangeStatus
import com.example.cote.data.remote.ReqChangeClassName
import com.example.cote.data.remote.ReqCheckOtp
import com.example.cote.data.remote.ReqOtpNum
import com.example.cote.data.remote.ReqReadAstin
import com.example.cote.data.remote.ReqUpdateStudent
import com.example.cote.data.remote.ResAddAdmin
import com.example.cote.data.remote.ResAddAstin
import com.example.cote.data.remote.ResAddClass
import com.example.cote.data.remote.ResAddStudent
import com.example.cote.data.remote.ResAttendanceChangeStatus
import com.example.cote.data.remote.ResChangeClassName
import com.example.cote.data.remote.ResCheckOtp
import com.example.cote.data.remote.ResDeleteAdmin
import com.example.cote.data.remote.ResDeleteClass
import com.example.cote.data.remote.ResDeleteStudent
import com.example.cote.data.remote.ResOtp
import com.example.cote.data.remote.ResReadAdmins
import com.example.cote.data.remote.ResReadAstin
import com.example.cote.data.remote.ResReadAttendance
import com.example.cote.data.remote.ResReadClass
import com.example.cote.data.remote.ResReadOneStudent
import com.example.cote.data.remote.ResReadStudent
import com.example.cote.data.remote.ResSendSms
import com.example.cote.data.remote.ResUpdateStudent
import com.example.cote.data.remote.StudentFullData
import com.example.cote.domain.repo.CoteRepo
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.HttpException
import java.math.BigInteger
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
            Log.d("SRESR" , res.toString())

            if (res.isSuccessful) {
                res.body()
            } else {

                val ebody  = res.errorBody()?.string()?.let {
                    Gson().fromJson(it, ResReadStudent::class.java)
                }

                ebody
            }
        }.onFailure {

            Log.d("SRESR" , it.toString() )

            handleError(it)
        }

    override suspend fun SendSms(token: String): Result<ResSendSms?>  =
        runCatching {
            val res = api.SnedSms(token )

            if (res.isSuccessful) {
                res.body()
            } else {

                val ebody  = res.errorBody()?.string()?.let {
                    Gson().fromJson(it, ResSendSms::class.java)
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

    override suspend fun ReadAstin(token: String, uid: String): Result<ResReadAstin?>  =
        runCatching {

            Log.d("rese" , uid)

            val res = api.ReadAstin(token , uid)

            if (res.isSuccessful) {
                Log.d("rese" , res.body()!!.message)
                res.body()
            } else {

                val ebody  = res.errorBody()?.string()?.let {
                    Gson().fromJson(it, ResReadAstin::class.java)
                }

                ebody
            }
        }.onFailure {
            handleError(it)
        }


    override suspend fun ReadAttendance(token: String, date: String): Result<ResReadAttendance?>    =
        runCatching {


        val res = api.ReadAttendance(token , date)

        if (res.isSuccessful) {
            res.body()
        } else {

            val ebody  = res.errorBody()?.string()?.let {
                Gson().fromJson(it, ResReadAttendance::class.java)
            }

            ebody
        }
    }.onFailure {
        handleError(it)
    }

    override suspend fun ChangeStatusAttendance(
        token: String,
        data: ReqAttendanceChangeStatus
    ): Result<ResAttendanceChangeStatus?>  =
        runCatching {


            val res = api.ChangeStatusAttendance(token , data)

            if (res.isSuccessful) {
                res.body()
            } else {

                val ebody  = res.errorBody()?.string()?.let {
                    Gson().fromJson(it, ResAttendanceChangeStatus::class.java)
                }

                ebody
            }
        }.onFailure {
            handleError(it)
        }

    override suspend fun ReadClass(token: String): Result<ResReadClass?>  =
        runCatching {


            val res = api.ReadClass(token)

            if (res.isSuccessful) {
                res.body()
            } else {

                val ebody  = res.errorBody()?.string()?.let {
                    Gson().fromJson(it, ResReadClass::class.java)
                }

                ebody
            }
        }.onFailure {
            handleError(it)
        }

    override suspend fun DeleteClass(token: String, id: String): Result<ResDeleteClass?>  =
        runCatching {


            val res = api.DeleteClass(token , id )

            if (res.isSuccessful) {
                res.body()
            } else {

                val ebody  = res.errorBody()?.string()?.let {
                    Gson().fromJson(it, ResDeleteClass::class.java)
                }

                ebody
            }
        }.onFailure {
            handleError(it)
        }

    override suspend fun ChangeClassName(
        token: String,
        id: String,
        NewName: String
    ): Result<ResChangeClassName?>  =
        runCatching {


            val res = api.ChangeClassName(token , ReqChangeClassName(id , NewName))

            if (res.isSuccessful) {
                res.body()
            } else {

                val ebody  = res.errorBody()?.string()?.let {
                    Gson().fromJson(it, ResChangeClassName::class.java)
                }

                ebody
            }
        }.onFailure {
            handleError(it)
        }

    override suspend fun AddClass(token: String, name: String): Result<ResAddClass?>  =
        runCatching {


            val res = api.AddClass(token , name)

            if (res.isSuccessful) {
                res.body()
            } else {

                val ebody  = res.errorBody()?.string()?.let {
                    Gson().fromJson(it, ResAddClass::class.java)
                }

                ebody
            }
        }.onFailure {
            handleError(it)
        }

    override suspend fun AddStudent(token: String, stu: ReqAddStudent): Result<ResAddStudent?> =
        runCatching {


            val res = api.AddStudent(token , stu)

            if (res.isSuccessful) {
                res.body()
            } else {

                val ebody  = res.errorBody()?.string()?.let {
                    Gson().fromJson(it, ResAddStudent::class.java)
                }

                ebody
            }
        }.onFailure {
            handleError(it)
        }

    override suspend fun DeleteStudent(token: String, id: String): Result<ResDeleteStudent?>  =
        runCatching {


            val res = api.DeleteStudent(token , id)

            if (res.isSuccessful) {
                res.body()
            } else {

                val ebody  = res.errorBody()?.string()?.let {
                    Gson().fromJson(it, ResDeleteStudent::class.java)
                }

                ebody
            }
        }.onFailure {
            handleError(it)
        }


    override suspend fun ReadOneStudent(token: String, id: String): Result<ResReadOneStudent?>  =
        runCatching {


            val res = api.ReadOneStudent(token , id)

            if (res.isSuccessful) {
                res.body()
            } else {

                val ebody  = res.errorBody()?.string()?.let {
                    Gson().fromJson(it, ResReadOneStudent::class.java)
                }

                ebody
            }
        }.onFailure {
            handleError(it)
        }

    override suspend fun UpdateStudent(
        token: String,
        id: String,
        stu: ReqAddStudent
    ): Result<ResUpdateStudent?>  =
        runCatching {


            val res = api.UpdateStudent(token , ReqUpdateStudent(id , stu ))

            if (res.isSuccessful) {
                res.body()
            } else {

                val ebody  = res.errorBody()?.string()?.let {
                    Gson().fromJson(it, ResUpdateStudent::class.java)
                }

                ebody
            }
        }.onFailure {
            handleError(it)
        }

    override suspend fun AddAdmin(token: String, admin: ReqAddAdmin): Result<ResAddAdmin?>  =
        runCatching {


            val res = api.AddAdmin(token , admin)

            if (res.isSuccessful) {
                res.body()
            } else {

                val ebody  = res.errorBody()?.string()?.let {
                    Gson().fromJson(it, ResAddAdmin::class.java)
                }

                ebody
            }
        }.onFailure {
            handleError(it)
        }

    override suspend fun DeleteAdmin(token: String, id: String): Result<ResDeleteAdmin?>  =
        runCatching {


            val res = api.DeleteAdmin(token , id)

            if (res.isSuccessful) {
                res.body()
            } else {

                val ebody  = res.errorBody()?.string()?.let {
                    Gson().fromJson(it, ResDeleteAdmin::class.java)
                }

                ebody
            }
        }.onFailure {
            handleError(it)
        }

    override suspend fun ReadAdmins(token: String): Result<ResReadAdmins?> =
        runCatching {


            val res = api.ReadAdmin(token )

            if (res.isSuccessful) {
                res.body()
            } else {

                val ebody  = res.errorBody()?.string()?.let {
                    Gson().fromJson(it, ResReadAdmins::class.java)
                }

                ebody
            }
        }.onFailure {
            handleError(it)
        }



    override suspend fun ReadNFCOnlyUID(context: Context): String? = suspendCancellableCoroutine { cont ->

        val nfcAdapter = NfcAdapter.getDefaultAdapter(context)
        if (nfcAdapter == null) {
            cont.resume(null) // Device doesn't support NFC
            return@suspendCancellableCoroutine
        }

        val activity = context as? Activity
        if (activity == null) {
            cont.resume(null) // Context is not an Activity
            return@suspendCancellableCoroutine
        }

        val callback = NfcAdapter.ReaderCallback { tag ->
            val uid = tag.id.joinToString("") { "%02X".format(it) }


            val convertedUid = uid
                .chunked(2)
                .reversed()
                .joinToString("")
                .toLong(16)
                .toString()
                .padStart(10, '0')

            cont.resume(convertedUid)
        }

        // Enable reader mode
        nfcAdapter.enableReaderMode(
            activity,
            callback,
            NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_NFC_B or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
            null
        )

        // If coroutine is cancelled, disable reader mode
        cont.invokeOnCancellation {

        }
    }

    override suspend fun readNfcTag(context: Context): String? =
        suspendCancellableCoroutine { cont ->

            val activity = context as? Activity
                ?: return@suspendCancellableCoroutine cont.resume(null, null)

            val adapter = NfcAdapter.getDefaultAdapter(activity)
                ?: return@suspendCancellableCoroutine cont.resume(null, null)

            val callback = NfcAdapter.ReaderCallback { tag ->
                try {
                    val uidHex = tag.id.joinToString("") { "%02X".format(it) }

                    val bytes = uidHex.chunked(2).map { it.toInt(16).toByte() }
                    val reversedBytes = bytes.reversed()

                    var decimal = 0L
                    for (b in reversedBytes) {
                        decimal = (decimal shl 8) or (b.toInt() and 0xFF).toLong()
                    }

                    val uid = decimal.toString().padStart(10, '0')

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
