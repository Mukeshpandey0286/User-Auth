package com.example.eyespecx

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eyespecx.databinding.ActivitySendOtpactivityBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class SendOTPActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySendOtpactivityBinding
    private lateinit var phoneNumberET : EditText
    private lateinit var auth : FirebaseAuth
    private lateinit var number : String
    private lateinit var mProgressBar : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        phoneNumberET = binding.inputMobile
        mProgressBar = binding.progressBar

        binding.buttonGetOTP.setOnClickListener {
            sendOtp()
        }
    }

    private fun sendOtp() {
        number = phoneNumberET.text.trim().toString()
        if (number.isNotEmpty() && number.length == 10) {
            number = "+91$number"
            mProgressBar.visibility = View.VISIBLE
            binding.buttonGetOTP.text = ""
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(number)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(callbacks)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        } else {
            Toast.makeText(this, "Please enter a correct 10-digit number", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    mProgressBar.visibility = View.INVISIBLE
                    binding.buttonGetOTP.text = "GET OTP"
                    Toast.makeText(this, "Authentication successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this , MainActivity::class.java))
                     finish()
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        mProgressBar.visibility = View.INVISIBLE
                        Toast.makeText(this, "Invalid verification code", Toast.LENGTH_SHORT).show()
                    } else {
                        mProgressBar.visibility = View.INVISIBLE
                        Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                    }
                }
                mProgressBar.visibility = View.INVISIBLE
                binding.buttonGetOTP.text = "GET OTP"
            }
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // Automatic verification or code retrieval
            // signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(this@SendOTPActivity, "Verification failed: ${e.message}", Toast.LENGTH_SHORT).show()
            mProgressBar.visibility = View.INVISIBLE
            binding.buttonGetOTP.text = "GET OTP"
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(verificationId, token)
            // Store the verification ID if needed for manual code entry

            val intent = Intent(this@SendOTPActivity , VerifyOTPActivity::class.java)
            intent.putExtra("OTP" , verificationId)
            intent.putExtra("resendToken" , token.toString()) // Convert token to string if needed
            intent.putExtra("phoneNumber" , number)
            Toast.makeText(this@SendOTPActivity, "OTP sent successfully!", Toast.LENGTH_SHORT).show()
            startActivity(intent)
            finish()
        }
    }

}
