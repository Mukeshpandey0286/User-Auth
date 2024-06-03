package com.example.eyespecx

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eyespecx.Data.ApiInterface
import com.example.eyespecx.Data.dataModel
import com.example.eyespecx.databinding.ActivityEmailLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Email_Login : AppCompatActivity() {
    private lateinit var binding: ActivityEmailLoginBinding
    var auth = false
    private lateinit var mProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://apitest-ggso.onrender.com/api/")
            .build().create(ApiInterface::class.java)

        var name = binding.editTextName
        var email = binding.editTextTextEmailAddress
        var password = binding.editTextTextPassword
        mProgressBar = binding.progressBar2
        mProgressBar.visibility = View.INVISIBLE

        binding.createUser.setOnClickListener {
            Log.d("data", "onCreate: ${email} : ${password}")
            if (name.text.isNotEmpty() && email.text.isNotEmpty() && password.text.isNotEmpty()) {
                if (password.text.length > 6) {
                    mProgressBar.visibility = View.VISIBLE
                    var data = dataModel(
                        name.text.toString(),
                        email.text.toString(),
                        password.text.toString()
                    )
                    retrofit.createUser(data)!!.enqueue(object : Callback<dataModel?> {
                        override fun onResponse(
                            call: Call<dataModel?>?,
                            response: Response<dataModel?>
                        ) {
                            Log.d("user", "onResponse: ${data}")
                            val model: dataModel? = response.body()
                            if (response.isSuccessful) {
                                PreferenceHelper.setLoggedIn(this@Email_Login, true)
                                val intent = Intent(this@Email_Login, MainActivity::class.java)
                                startActivity(intent)
                                auth = true
                                finish()
                            } else {
                                Log.d("TAG", "onResponse: error")
                            }
                        }

                        override fun onFailure(call: Call<dataModel?>?, t: Throwable) {
                            auth = false
                            mProgressBar.visibility = View.INVISIBLE
                            Log.d("error", "onError: ${t.message}")
                        }
                    })
                }
                else{
                    Toast.makeText(this, "Password must be greater than 6 characters", Toast.LENGTH_SHORT).show()
                }

            }
            else{
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            }
            mProgressBar.visibility = View.INVISIBLE

        }

        binding.loginUi.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
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