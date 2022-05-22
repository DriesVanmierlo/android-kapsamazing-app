package org.vanmierlo.kapsamazing_app

import android.os.Bundle
import android.util.Log.d
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import org.vanmierlo.kapsamazing_app.databinding.ActivityMainBinding
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val kapsalonViewModel: MainViewModel by viewModels {
        KapsalonViewModelFactory((application as KapsalonsApplication).repository)
    }

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

        var kapsalonsList = listOf<Kapsalon>()
        var kapsalonAdapter = KapsalonsAdapter(kapsalonsList)

        api.getAllKapsalons().enqueue(object : Callback<List<Kapsalon>>{
            override fun onResponse(
                call: Call<List<Kapsalon>>,
                response: Response<List<Kapsalon>>
            ) {
                showData(response.body()!!)
//                d("kapsalon","onResponse: ${response.body()!![0].name}")

                //DELETE ALL CURRENT KAPSALONS
                kapsalonViewModel.deleteAll()


                //INSERT LOADED KAPSALONS TO ROOM
                for (kapsalon in response.body()!!){
                    kapsalonsList += kapsalon
                    kapsalonViewModel.insert(Kapsalon(kapsalon.name, kapsalon.city, kapsalon.restaurant, kapsalon.type, kapsalon.delivered, kapsalon.price, kapsalon.kapid, kapsalon.latestGeneralRating, kapsalon.image))
                }

            }

            override fun onFailure(call: Call<List<Kapsalon>>, t: Throwable) {
                d("kapsalon", "onFailure")

                if(kapsalonsList.isNotEmpty()){
                    loadRoomKapsalons()
                }

            }

        })

//        fun loadRoomKapsalons(){
//            kapsalonViewModel.allKapsalons.observe(this) {
//                    kapsalons -> kapsalons?.let { kapsalonsList = it }
//                kapsalonAdapter.kapsalons = kapsalons
//                kapsalonAdapter.notifyDataSetChanged()
//            }
//            showData(kapsalonsList)
//        }

//        val kapsalons = mutableListOf<Kapsalon>()
//        for (i in 0..100){
//            kapsalons.add(
//                Kapsalon("Kapsalon kebab","Bocholt", "Rana Kebab", "Kebab", listOf("geleverd", "afhalen"), 8.00,123456,  "https://i.ibb.co/4gZc6kY/kebab-tasty-food-vesalius.jpg")
//            )
//        }

//        val db = Room.databaseBuilder(
//            applicationContext,
//            AppDatabase::class.java, "kapsamazing-database"
//        ).build()
//
//        val kapsalonDao = db.kapsalonDao()
//        val kapsalonsLocal: List<KapsalonRow> = kapsalonDao.getAll()
    }

    fun loadRoomKapsalons(){
        var kapsalonsList = listOf<Kapsalon>()
        var kapsalonAdapter = KapsalonsAdapter(kapsalonsList)

        kapsalonViewModel.allKapsalons.observe(this) {
                kapsalons -> kapsalons?.let { kapsalonsList = it }
            kapsalonAdapter.kapsalons = kapsalons
            kapsalonAdapter.notifyDataSetChanged()
        }
        showData(kapsalonsList)
    }

    private fun showData(kapsalons: List<Kapsalon>) {
        val recyclerView: RecyclerView = findViewById(R.id.homeRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = KapsalonsAdapter(kapsalons)
    }
}