package com.dasonick.bukhaleva.interfaces

import com.dasonick.bukhaleva.model.CardsResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitServices {
    @GET("{type}/{index}?json=true")
    fun getCardList(@Path("type") type: String, @Path("index") index: Int): Call<CardsResult>
}