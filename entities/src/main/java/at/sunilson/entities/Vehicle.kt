package at.sunilson.entities

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Vehicle(val vin: String, val modelName: String, val imageUrl: String)