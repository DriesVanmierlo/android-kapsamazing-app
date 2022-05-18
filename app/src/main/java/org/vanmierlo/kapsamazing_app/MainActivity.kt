package org.vanmierlo.kapsamazing_app

import android.os.Bundle
import android.util.Log.d
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.vanmierlo.kapsamazing_app.databinding.ActivityMainBinding
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val recyclerView: RecyclerView = findViewById(R.id.homeRecyclerView)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://web2-kapsamazing-driesv.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)

        api.getAllKapsalons().enqueue(object : Callback<List<Kapsalon>>{
            override fun onResponse(
                call: Call<List<Kapsalon>>,
                response: Response<List<Kapsalon>>
            ) {
                showData(response.body()!!)
//                d("kapsalon","onResponse: ${response.body()!![0].name}")
            }

            override fun onFailure(call: Call<List<Kapsalon>>, t: Throwable) {
                d("kapsalon", "onFailure")
            }

        })

//        val kapsalons = mutableListOf<Kapsalon>()
//        for (i in 0..100){
//            kapsalons.add(
//                Kapsalon("Kapsalon kebab","Bocholt", "Rana Kebab", "Kebab", listOf("geleverd", "afhalen"), 8.00,123456,  "https://i.ibb.co/4gZc6kY/kebab-tasty-food-vesalius.jpg")
//            )
//        }
    }

    private fun showData(kapsalons: List<Kapsalon>) {
        val recyclerView: RecyclerView = findViewById(R.id.homeRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = KapsalonsAdapter(kapsalons)
    }
}