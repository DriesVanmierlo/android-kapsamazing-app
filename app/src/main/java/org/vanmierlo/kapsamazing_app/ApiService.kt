package org.vanmierlo.kapsamazing_app

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/kapsalons")
    fun getAllKapsalons():Call<List<Kapsalon>>

    @GET("/kapsalon?id={kapid}")
    fun getKapsalonByKapid(@Path("kapid") kapid: String):Call<List<Kapsalon>>
}