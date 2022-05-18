package org.vanmierlo.kapsamazing_app

data class Kapsalon(
    val name: String,
//    val city: String,
    val restaurant: String,
//    val type: String,
    val delivered: List<String>,
    val price: Double,
//    val kapid: Int,
    val latestGeneralRating: Double,
    val image: String
) {
}