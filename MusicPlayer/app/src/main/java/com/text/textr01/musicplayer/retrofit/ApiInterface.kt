package com.text.textr01.musicplayer.retrofit


import com.text.textr01.musicplayer.model.DataModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


//api interface for call data model through base url

interface ApiInterface {

    @GET("studio")
    fun getStudio(): Call<List<DataModel>>



}
