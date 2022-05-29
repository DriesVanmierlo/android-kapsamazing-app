package org.vanmierlo.kapsamazing_app

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("/kapsalons")
    fun getAllKapsalons():Call<List<Kapsalon>>

    @GET("/kapsalon?id={kapid}")
    fun getKapsalonByKapid(@Path("kapid") kapid: String):Call<List<Kapsalon>>

    @PUT("/rateKapsalon/{id}")
    suspend fun updateKapsalonRating(@Path("id") id: String, @Body requestBody: RequestBody): Response<ResponseBody>
}