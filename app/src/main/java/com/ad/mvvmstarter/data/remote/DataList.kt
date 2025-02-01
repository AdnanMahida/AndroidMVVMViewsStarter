package com.ad.mvvmstarter.data.remote


import com.google.gson.annotations.SerializedName

data class DeviceListItem(
    @SerializedName("data")
    val dataList: Data,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)

data class Data(
    @SerializedName("CPU model")
    val cPUModel: String,
    @SerializedName("capacity")
    val capacity: String,
    @SerializedName("capacity GB")
    val capacityGB: Int,
    @SerializedName("Case Size")
    val caseSize: String,
    @SerializedName("color")
    val color: String,
    @SerializedName("Description")
    val description: String,
    @SerializedName("generation")
    val generation: String,
    @SerializedName("Hard disk size")
    val hardDiskSize: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("Screen size")
    val screenSize: Double,
    @SerializedName("Strap Colour")
    val strapColour: String,
    @SerializedName("year")
    val year: Int
)