package com.example.eyespecx

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.graphics.scaleMatrix

class RunIntroActivity : IntroActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val titleArray = arrayOf(
            getString(R.string.slide_1_title), getString(R.string.slide_2_title),
            getString(R.string.slide_3_title)
        )

        val imageArray = arrayOf(
            getDrawable(R.drawable.driver),
            getDrawable(R.drawable.eyes_stores),
            getDrawable(R.drawable.specs_collection)
        )

        val dotInActiveColor = ContextCompat.getColor(this, R.color.dot_inactive)
        val dotActiveColor = ContextCompat.getColor(this, R.color.colorAccent)

        val subtitleText = getString(R.string.ready_to_order_from_your_favourite_eyewear)

        initSlide(titleArray, imageArray, subtitleText, dotInActiveColor, dotActiveColor)



        binding.getStartedBtn.setOnClickListener {
            startActivity(Intent(this, User_Intraction_AuthMethod::class.java))
            finish()
        }
    }

}