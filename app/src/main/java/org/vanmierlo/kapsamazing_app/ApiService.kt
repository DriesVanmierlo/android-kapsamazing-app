package org.vanmierlo.kapsamazing_app

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("/kapsalons")
    fun getAllKapsalons():Call<List<Kapsalon>>
}