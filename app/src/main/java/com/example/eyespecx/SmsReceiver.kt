package com.example.eyespecx

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val bundle = intent.extras
            if (bundle != null) {
                val pdus = bundle.get("pdus") as Array<*>
                for (pdu in pdus) {
                    val smsMessage = SmsMessage.createFromPdu(pdu as ByteArray)
                    if (smsMessage != null) {
                        val messageBody = smsMessage.messageBody
                        val otp = extractOTP(messageBody)
                        if (otp.isNotEmpty()) {
                            val verifyIntent = Intent(context, MainActivity::class.java)
                            verifyIntent.putExtra("otp", otp)
                            verifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            context?.startActivity(verifyIntent)
                            break // Exit the loop after extracting OTP from the first message
                        }
                    }
                }
            }
        }
    }

    private fun extractOTP(messageBody: String?): String {
        messageBody?.let {
            val regex = "\\b\\d{6}\\b".toRegex()
            val matchResult = regex.find(it)
            return matchResult?.value ?: ""
        }
        return ""
    }
}
