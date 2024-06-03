package com.example.eyespecx.Data

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {
  @POST("users")
  fun createUser(@Body dataModel: dataModel): Call<dataModel>

  @POST("register")
  fun registerUser(@Body loginUser: loginUser): Call<loginUser>

}