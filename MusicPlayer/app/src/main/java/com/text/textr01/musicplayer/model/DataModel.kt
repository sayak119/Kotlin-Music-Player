package com.text.textr01.musicplayer.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

//data model class to collect data from web api
data class DataModel(


    @Expose
    @SerializedName("song")
    val song: String,
    @Expose
    @SerializedName("url")
    val url: String,
    @Expose
    @SerializedName("cover_image")
    val cover_image: String,
    @Expose
    @SerializedName("artists")
    val artists: String
)