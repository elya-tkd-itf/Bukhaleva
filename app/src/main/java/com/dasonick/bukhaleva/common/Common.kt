package com.dasonick.bukhaleva.common

import com.dasonick.bukhaleva.interfaces.RetrofitServices
import com.dasonick.bukhaleva.retrofiit.RetrofitClient

object Common {
    private const val BASE_URL = "https://developerslife.ru/"
    val retrofitService: RetrofitServices
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitServices::class.java)
}