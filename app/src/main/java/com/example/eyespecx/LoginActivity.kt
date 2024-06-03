package com.example.eyespecx

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eyespecx.Data.ApiInterface
import com.example.eyespecx.Data.dataModel
import com.example.eyespecx.Data.loginUser
import com.example.eyespecx.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    var auth = false
    private lateinit var  mProgressBar : ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mProgressBar = binding.progressBar
        val retrofit = Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://apitest-ggso.onrender.com/api/users/")
            .build().create(ApiInterface::class.java)

        binding.signupUi.setOnClickListener {
            val intent = Intent(this, Email_Login::class.java)
            startActivity(intent)
            finish()
        }

        binding.loginUser.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                if (password.length > 6) {
                    mProgressBar.visibility = ProgressBar.VISIBLE
                    val data = loginUser(email, password)
                    retrofit.registerUser(data)!!.enqueue(object : Callback<loginUser?> {
                        override fun onResponse(
                            call: Call<loginUser?>?,
                            response: Response<loginUser?>
                        ) {
                            val res = response.body()
                            Log.d("loginUser", "onResponse: ${data}")
                            if (response.code() == 200) {
                                Log.d("loginPass", "onResponse: ${res} : ${password}")
                                PreferenceHelper.setLoggedIn(this@LoginActivity, true)
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                auth = true
                                finish()
                            } else {
                                Log.d("loginErr", "onResponse: error${response.errorBody().toString()}")
                                auth = false
                                mProgressBar.visibility = ProgressBar.INVISIBLE
                                Toast.makeText(this@LoginActivity, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<loginUser?>?, t: Throwable) {
                            Log.d("error", "onError: ${t.message}")
                            mProgressBar.visibility = ProgressBar.INVISIBLE
                        }
                    })
                } else {
                    Toast.makeText(this, "Password must be greater than 6 characters", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (auth) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
