package com.example.eyespecx

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eyespecx.databinding.ActivityUserIntractionAuthMethodBinding

class User_Intraction_AuthMethod : AppCompatActivity() {
    private lateinit var binding: ActivityUserIntractionAuthMethodBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserIntractionAuthMethodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginPhone.setOnClickListener{
            val intent = Intent(this, SendOTPActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.loginEmail.setOnClickListener {
            val intent = Intent(this, Email_Login::class.java)
            startActivity(intent)
            finish()
        }
    }
}