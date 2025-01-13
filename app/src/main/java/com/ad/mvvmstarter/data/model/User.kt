package com.ad.mvvmstarter.data.model


/**
 * User class
 * */
data class User(
    val accessToken: String,
    val userId: Int,
    val username: String,
    val firstname: String?,
    val lastname: String?,
    val email: String?,
    val phoneNumber: String?
)