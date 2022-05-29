package org.vanmierlo.kapsamazing_app

import android.content.Intent
import android.content.Intent.EXTRA_TEXT
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Log.d
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.math.RoundingMode
import java.util.concurrent.Executors
import kotlin.math.roundToInt

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val kapid = intent.getStringExtra(EXTRA_TEXT)
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
                        showData(kapsalonDetails)
                    }
                }
//                showData(response.body()!!.sortedByDescending { it.latestGeneralRating })
//                d("kapsalon","onResponse: ${response.body()!![0].name}")
            }

            override fun onFailure(call: Call<List<Kapsalon>>, t: Throwable) {
                d("kapsalon", "onFailure")

//                if(kapsalonsList.isNotEmpty()){
//                    loadRoomKapsalons(kapsalonsList)
//                }

            }

        })
    }

    fun showData(details: Kapsalon){
        println(details)
        println(details.ratings)

        val title = findViewById<TextView>(R.id.detailTitleText)
        val restaurant = findViewById<TextView>(R.id.detailRestaurantText)
        val imageView = findViewById<ImageView>(R.id.detailKapsalonImage)
        val price = findViewById<TextView>(R.id.detailPriceText)
        val rating = findViewById<TextView>(R.id.detailRatingText)

        val friesScore = findViewById<TextView>(R.id.scoreFriesText)
        val meatScore = findViewById<TextView>(R.id.scoreMeatText)
        val toppingsScore = findViewById<TextView>(R.id.scoreToppingsText)

        val orderButton = findViewById<Button>(R.id.orderButton)

        title.text = details.name
        restaurant.text = details.restaurant
        price.text = "€" + details.price

        if(!details.ratings.isEmpty()){
            rating.text = details.latestGeneralRating.toString() + "/5"
            friesScore.text = calculateAverageScore(details,"fries") + "/5"
            meatScore.text = calculateAverageScore(details, "meat") + "/5"
            toppingsScore.text = calculateAverageScore(details, "toppings") + "/5"
        } else {
            rating.text = "?/5"
            friesScore.text = "?/5"
            meatScore.text = "?/5"
            toppingsScore.text = "?/5"
        }


        orderButton.setOnClickListener(){
            val implicitIntent = Intent(Intent.ACTION_VIEW)
            implicitIntent.data = Uri.parse(details.link)
            startActivity(implicitIntent)
        }

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



    }

    fun calculateAverageScore(details: Kapsalon, ingredient: String): String{
        var score: Double = 0.0
        var totalRatings: Int = 0
        if (ingredient == "fries"){
            for (r in details.ratings){
                score += r.fries
                totalRatings ++
            }
        } else if (ingredient == "meat"){
            for (r in details.ratings){
                score += r.meat
                totalRatings ++
            }
        } else {
            for (r in details.ratings){
                score += r.toppings
                totalRatings ++
            }
        }
        score = score / totalRatings
        return score.toBigDecimal().setScale(1, RoundingMode.UP).toString()
    }
}