package org.vanmierlo.kapsamazing_app

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.text.TextRunShaper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.concurrent.Executors

class RatingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

        val kapid = intent.getStringExtra(Intent.EXTRA_TEXT)
        println(kapid)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://web2-kapsamazing-driesv.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)

        var kapsalonsDetails: List<Kapsalon> = mutableListOf<Kapsalon>()
        var kapsalonDetails: Kapsalon

        api.getAllKapsalons().enqueue(object : Callback<List<Kapsalon>> {
            override fun onResponse(
                call: Call<List<Kapsalon>>,
                response: Response<List<Kapsalon>>
            ) {
                kapsalonsDetails = response.body()!!
                for (kapsalon in kapsalonsDetails){
                    if (kapsalon.kapid == kapid){
                        kapsalonDetails = kapsalon
                        return showData(kapsalonDetails)
                    } else {
                        val title = findViewById<TextView>(R.id.ratingTitleText)
                        title.text = "This code does not exist"
                    }
                }
            }

            override fun onFailure(call: Call<List<Kapsalon>>, t: Throwable) {
                Log.d("kapsalon", "onFailure")
            }
        })

    }

    fun getProgress(bar: SeekBar): Double {
        var progress: Double = bar.progress / 2.0
        println(progress)
        return progress
    }

    fun buildRequest(friesR: Double, meatR: Double, toppingsR: Double, currentRatings: List<Rating>){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://web2-kapsamazing-driesv.herokuapp.com/")
            .build()

        val api = retrofit.create(ApiService::class.java)

        var allRatings: MutableList<Rating> = currentRatings.toMutableList()
        allRatings.add(Rating(friesR, meatR, toppingsR))

        var newLatestGeneralScore = calculateNewGeneralScore(allRatings)

        val jsonObject = JSONObject()
        jsonObject.put("ratings", allRatings.toString())
        jsonObject.put("latestGeneralScore", "iOS Developer")
    }

    fun calculateNewGeneralScore(ratings: MutableList<Rating>): BigDecimal? {
        var latestRating: Double = 0.0
        var ratingsNumber: Int = 0
        for (r in ratings){
            latestRating = latestRating + r.fries + r.meat + r.toppings
            ratingsNumber ++
        }
        latestRating = latestRating / 3 / ratingsNumber
        println(latestRating.toBigDecimal().setScale(1, RoundingMode.UP).toString())
        return latestRating.toBigDecimal().setScale(1, RoundingMode.UP)
    }

    fun showData(details: Kapsalon){
        val title = findViewById<TextView>(R.id.ratingTitleText)
        val imageView = findViewById<ImageView>(R.id.ratingImage)

        title.text = "Rate " + details.name + " from " + details.restaurant

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        var image: Bitmap? = null
        executor.execute {
            val imageUrl = details.image

            try {
                val `in` = java.net.URL(imageUrl).openStream()
                image = BitmapFactory.decodeStream(`in`)

                handler.post {
                    imageView.setImageBitmap(image)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val friesBar = findViewById<SeekBar>(R.id.seekBarFries)
        val meatBar = findViewById<SeekBar>(R.id.seekBarMeat)
        val toppingsBar = findViewById<SeekBar>(R.id.seekBarToppings)

        val submitButton = findViewById<Button>(R.id.submitRatingButton)

        submitButton.setOnClickListener(){
            var friesRating = getProgress(friesBar)
            var meatRating = getProgress(meatBar)
            var toppingsRating = getProgress(toppingsBar)

            var currentRatings: List<Rating> = details.ratings

            buildRequest(friesRating, meatRating, toppingsRating, currentRatings)
        }
    }
}
