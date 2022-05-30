package org.vanmierlo.kapsamazing_app

import java.math.BigDecimal

data class SaveRatingRequest(
    val ratings: String,
    val latestGeneralRating: Int
) {
}