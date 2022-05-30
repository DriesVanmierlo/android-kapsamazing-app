package org.vanmierlo.kapsamazing_app

import java.util.*

data class Kapsalon(
    val _id: String,
    val name: String,
    val city: String,
    val restaurant: String,
    val type: String,
    val delivered: List<String>,
    val price: Double,
    val ratings: List<Rating>,
    val kapid: String,
    val latestGeneralRating: Double,
    val image: String,
    val link: String
) {
}