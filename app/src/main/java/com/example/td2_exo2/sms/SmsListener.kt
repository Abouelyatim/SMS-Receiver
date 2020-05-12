package com.example.td2_exo2.sms

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.telephony.SmsMessage
import android.util.Log
import com.example.td2_exo2.MainActivity.Companion.contact
import com.example.td2_exo2.mail.MailStructure
import com.example.td2_exo2.mail.MailUtils
import com.example.td2_exo2.notification.NotificationStructure
import com.example.td2_exo2.notification.NotificationUtils


class SmsListener : BroadcastReceiver() {
    private var msgBody: String? = null
    private var msgSource: String? = null
    private val preferences: SharedPreferences? = null

    override fun onReceive(
        context: Context,
        intent: Intent
    ) { // TODO Auto-generated method stub
        if (intent.action == "android.provider.Telephony.SMS_RECEIVED") {
            //Toast.makeText(context, "message received", Toast.LENGTH_SHORT).show()
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
                            val sendMail=MailUtils(
                                item.mail,
                                MailStructure.subject,
                                MailStructure.text)

                            val mNotificationUtils =
                                NotificationUtils(
                                    context
                                )

                            val nb: Notification.Builder? = mNotificationUtils.getAndroidChannelNotification(
                                NotificationStructure.title, NotificationStructure.body+item.mail)
                            mNotificationUtils.getManager().notify(101,nb!!.build())

                            //Toast.makeText(context, "success", Toast.LENGTH_LONG).show()


                        }
                    }


                }
            } catch (e: Exception) {
                Log.d("Exception caught", e.message)
            }
        }
    }




}