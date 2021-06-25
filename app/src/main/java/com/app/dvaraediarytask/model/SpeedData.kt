package com.app.dvaraediarytask.model

import com.google.firebase.database.IgnoreExtraProperties
import com.google.gson.annotations.SerializedName

@IgnoreExtraProperties
class SpeedData(@field:SerializedName("id") var id: String,
                @field:SerializedName("datetime") var datetime: String,
                @field:SerializedName("timestamp") var timestamp: String,
                @field:SerializedName("upspeed") var upspeed: String,
                @field:SerializedName("mobileno") var mobileno: String)