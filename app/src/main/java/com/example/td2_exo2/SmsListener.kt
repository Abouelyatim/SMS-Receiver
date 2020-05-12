package com.example.td2_exo2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import com.example.td2_exo2.MainActivity.Companion.contact


class SmsListener : BroadcastReceiver() {
    private var msgBody: String? = null
    private var msgSource: String? = null
    private val preferences: SharedPreferences? = null

    override fun onReceive(
        context: Context,
        intent: Intent
    ) { // TODO Auto-generated method stub
        if (intent.action == "android.provider.Telephony.SMS_RECEIVED") {
            Toast.makeText(context, "message received", Toast.LENGTH_SHORT).show()
            val bundle = intent.extras
            try {
                if (bundle != null) {
                    val pdus =
                        bundle["pdus"] as Array<*>?
                    for (i in pdus!!.indices) {
                        var smsMessage: SmsMessage
                        smsMessage =
                            SmsMessage.createFromPdu(
                                pdus[i] as ByteArray,
                                bundle.getString("format")
                            )
                        val msg_from = smsMessage.displayOriginatingAddress
                        msgBody = smsMessage.messageBody
                        msgSource=smsMessage.displayOriginatingAddress
                    }

                    for(item in contact){

                        if(item.num.equals("0"+msgSource!!.subSequence(4,msgSource!!.length).replace("\\s".toRegex(), ""))){
                            Toast.makeText(context, "message is:$msgBody// source: $msgSource", Toast.LENGTH_LONG).show()

                        }
                    }


                }
            } catch (e: Exception) {
                Log.d("Exception caught", e.message)
            }
        }
    }

    fun isOnContact(contact:String){


    }
}