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
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
//                showData(response.body()!!.sortedByDescending { it.latestGeneralRating })
//                d("kapsalon","onResponse: ${response.body()!![0].name}")
            }

            override fun onFailure(call: Call<List<Kapsalon>>, t: Throwable) {
                Log.d("kapsalon", "onFailure")

//                if(kapsalonsList.isNotEmpty()){
//                    loadRoomKapsalons(kapsalonsList)
//                }

            }

        })

        val friesBar = findViewById<SeekBar>(R.id.seekBarFries)
        friesBar.setOnSeekBarChangeListener(this)
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
    }

    fun getValue(){
        val friesBar = findViewById<SeekBar>(R.id.seekBarFries)
        println(friesBar.progress)
    }
}

private fun SeekBar.setOnSeekBarChangeListener(ratingActivity: RatingActivity) {
    val friesBar = findViewById<SeekBar>(R.id.seekBarFries)
    println(friesBar.progress)
}
