package org.vanmierlo.kapsamazing_app

data class Kapsalon(
    val name: String,
    val restaurant: String,
    val options: List<String>,
    val price: Double,
    val image: String
) {
}