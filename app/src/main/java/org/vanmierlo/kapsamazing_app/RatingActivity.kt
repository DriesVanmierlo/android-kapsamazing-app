package org.vanmierlo.kapsamazing_app

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
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

    fun buildRequest(friesR: Double, meatR: Double, toppingsR: Double, currentRatings: List<Rating>, kapsalon: Kapsalon){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://web2-kapsamazing-driesv.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)

        var allRatings: MutableList<Rating> = currentRatings.toMutableList()
        allRatings.add(Rating(friesR, meatR, toppingsR))

        var newLatestGeneralScore = calculateNewGeneralScore(allRatings)
        var ratingsArray = arrayListOf<Rating>()

        for (r in allRatings){
            ratingsArray.add(r)
        }

        var saveRatingRequest = SaveRatingRequest(Gson().toJson(ratingsArray), newLatestGeneralScore!!.toInt())

        val jsonObject = JSONObject()
        jsonObject.put("ratings", saveRatingRequest.ratings)
        jsonObject.put("latestGeneralRating", saveRatingRequest.latestGeneralRating)

        val jsonObjectString = jsonObject.toString()
        println(jsonObjectString)

        val requestBody: RequestBody = jsonObjectString.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())


        CoroutineScope(Dispatchers.IO).launch {

            // Do the PUT request and get response
            val response = api.updateKapsalonRating(id = kapsalon._id, requestBody)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    // Convert raw JSON to pretty JSON using GSON library
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()
                                ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                        )
                    )

                    Log.d("Pretty Printed JSON :", prettyJson)

                } else {

                    Log.e("RETROFIT_ERROR", response.code().toString())

                }
            }
        }

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
            println(currentRatings)
            println(details.ratings)

            buildRequest(friesRating, meatRating, toppingsRating, currentRatings, details)
            var toast = Toast.makeText(applicationContext, "Saved your rating!", Toast.LENGTH_LONG).show()
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
