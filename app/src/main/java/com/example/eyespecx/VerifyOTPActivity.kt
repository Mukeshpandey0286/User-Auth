package com.example.eyespecx

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.telephony.SmsMessage
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eyespecx.databinding.ActivityVerifyOtpBinding

class VerifyOTPActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerifyOtpBinding
    private lateinit var otpEditTexts: Array<EditText>
    private lateinit var smsReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize EditTexts for OTP input
        otpEditTexts = arrayOf(
            binding.inputCode1,
            binding.inputCode2,
            binding.inputCode3,
            binding.inputCode4,
            binding.inputCode5,
            binding.inputCode6
        )

        // Register SMS receiver
        registerSmsReceiver()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(smsReceiver)
    }

    private fun registerSmsReceiver() {
        smsReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == "android.provider.Telephony.SMS_RECEIVED") {
                    val bundle = intent.extras
                    if (bundle != null) {
                        val pdus = bundle["pdus"] as Array<*>
                        for (pdu in pdus) {
                            val smsMessage = SmsMessage.createFromPdu(pdu as ByteArray)
                            val messageBody = smsMessage.messageBody
                            val otp = extractOTP(messageBody)
                            if (otp.isNotEmpty()) {
                                autoFillAndVerifyOTP(otp)
                                break
                            }
                        }
                    }
                }
            }
        }
        registerReceiver(smsReceiver, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
    }

    private fun extractOTP(messageBody: String): String {
        val regex = "\\b\\d{6}\\b".toRegex()
        val matchResult = regex.find(messageBody)
        return matchResult?.value ?: ""
    }

     fun autoFillAndVerifyOTP(otp: String) {
        for ((index, editText) in otpEditTexts.withIndex()) {
            if (index < otp.length) {
                editText.setText(otp[index].toString())
            }
        }

        verifyOTP(otp)
    }
var userAuth = false
    private fun verifyOTP(otp: String) {
        PreferenceHelper.setLoggedIn(this, true)
        startActivity(Intent(this, MainActivity::class.java))
        Toast.makeText(this, "OTP Verification Successful: $otp", Toast.LENGTH_SHORT).show()
        userAuth = true
    finish()

    }

    override fun onResume() {
        super.onResume()
        if (userAuth) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
