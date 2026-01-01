package com.example.school.data.model

import android.app.Activity
import android.content.Context
import android.nfc.NfcAdapter


class NfcManager(private val context: Context){

    private val adapter = NfcAdapter.getDefaultAdapter(context)

    var onTagRead : ((String) -> Unit)? = null

    fun enableReader(activity : Activity){


        val callBack = NfcAdapter.ReaderCallback { tag ->
            val uid = tag.id.joinToString("") { "%02X".format(it) }

            activity.runOnUiThread{
                onTagRead?.invoke(uid)
            }
        }

        adapter.enableReaderMode(
            activity ,
            callBack ,
            NfcAdapter.FLAG_READER_NFC_A or
            NfcAdapter.FLAG_READER_NFC_B ,
            null
        )

    }

    fun disableReader(activity: Activity){
        adapter.disableReaderMode(activity)
    }


}
