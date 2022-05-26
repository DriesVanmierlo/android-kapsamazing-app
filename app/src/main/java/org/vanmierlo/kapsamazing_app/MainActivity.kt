package org.vanmierlo.kapsamazing_app

import android.os.Bundle
import android.util.Log.d
import android.widget.CheckBox
import android.widget.SearchView
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
import org.vanmierlo.kapsamazing_app.data.KapsalonRoom
import org.vanmierlo.kapsamazing_app.databinding.ActivityMainBinding
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var kapsalons: List<Kapsalon> = listOf()

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

        var kapsalonsList = mutableListOf<Kapsalon>()
        var kapsalonsListRoom = listOf<KapsalonRoom>()
        var kapsalonRoomAdapter = KapsalonRoomAdapter(kapsalonsListRoom)

        api.getAllKapsalons().enqueue(object : Callback<List<Kapsalon>>{
            override fun onResponse(
                call: Call<List<Kapsalon>>,
                response: Response<List<Kapsalon>>
            ) {
                kapsalons = response.body()!!
                showData(response.body()!!)
//                d("kapsalon","onResponse: ${response.body()!![0].name}")

//                DELETE ALL CURRENT KAPSALONS
//                kapsalonViewModel.deleteAll()

                //INSERT LOADED KAPSALONS TO ROOM
                for (kapsalon: Kapsalon in response.body()!!){
                    kapsalonsList.add(kapsalon)
                }

                kapsalonsListRoom = convertToRoom(kapsalonsList)

                for (kapsalon in kapsalonsListRoom){
//                    kapsalonViewModel.insert(kapsalon)
//                    kapsalonViewModel.insert(KapsalonRoom(0,"test", "test", "test" , "test" , listOf("test", "test") , 3.0 , "test", 4.5, "test"))
                    println(kapsalon)
                }

                println("getItemCount:")
                println(kapsalonRoomAdapter.itemCount)
            }

            override fun onFailure(call: Call<List<Kapsalon>>, t: Throwable) {
                d("kapsalon", "onFailure")

//                if(kapsalonsList.isNotEmpty()){
//                    loadRoomKapsalons(kapsalonsList)
//                }

            }

        })

        var searchView: SearchView = findViewById(R.id.search_bar)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String?): Boolean {

                var filteredKapsalons: MutableList<Kapsalon> = mutableListOf()

                for (kapsalon in kapsalons){
                    if(kapsalon.restaurant.lowercase().contains(s.toString().lowercase()) || kapsalon.city.lowercase().contains(s.toString().lowercase())){
                        filteredKapsalons.add(kapsalon)
                    }
                }

                showData(filteredKapsalons)

                return false
            }

        })

        var checkboxPickup: CheckBox = findViewById(R.id.homeCheckboxPickup)
        var checkboxDelivery: CheckBox = findViewById(R.id.homeCheckboxDelivered)

        checkboxPickup.setOnClickListener(){
            filterCheckedKapsalons()
        }

        checkboxDelivery.setOnClickListener(){
            filterCheckedKapsalons()
        }


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

//    fun loadRoomKapsalons(kapsalonsList: List<Kapsalon>){
//        var kapsalonsList = listOf<Kapsalon>()
//        var kapsalonAdapter = KapsalonsAdapter(kapsalonsList)
//
//        kapsalonViewModel.allKapsalons.observe(this) {
//                kapsalons -> kapsalons?.let { kapsalonsList = it }
//            kapsalonAdapter.kapsalons = kapsalons
//            kapsalonAdapter.notifyDataSetChanged()
//        }
//        showData(kapsalonsList)
//    }

    fun filterCheckedKapsalons(){
        var filteredKapsalons: MutableList<Kapsalon> = mutableListOf()

        var checkboxPickup: CheckBox = findViewById(R.id.homeCheckboxPickup)
        var checkboxDelivery: CheckBox = findViewById(R.id.homeCheckboxDelivered)

        if (checkboxPickup.isChecked && !checkboxDelivery.isChecked){
            for (kapsalon in kapsalons){
                if(kapsalon.delivered.contains("pickup")){
                    filteredKapsalons.add(kapsalon)
                }
            }
            showData(filteredKapsalons)

        } else if (!checkboxPickup.isChecked && checkboxDelivery.isChecked){
            for (kapsalon in kapsalons){
                if(kapsalon.delivered.contains("delivery")){
                    filteredKapsalons.add(kapsalon)
                }
            }
            showData(filteredKapsalons)
        } else {
            showData(kapsalons)
        }
    }

    private fun showData(kapsalons: List<Kapsalon>) {
        val recyclerView: RecyclerView = findViewById(R.id.homeRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = KapsalonsAdapter(kapsalons)
    }

    fun convertToRoom(kapslonsList: List<Kapsalon>): List<KapsalonRoom>{
        var kapsalonsListRoom = mutableListOf<KapsalonRoom>()

        for (kapsalon in kapslonsList){
            kapsalonsListRoom.add(
                KapsalonRoom(
                    id = 0,
                    name = kapsalon.name,
                    city = kapsalon.city,
                    restaurant = kapsalon.restaurant,
                    type = kapsalon.type,
                    delivered = kapsalon.delivered,
                    price = kapsalon.price,
                    kapid = kapsalon.kapid,
                    latestGeneralRating = kapsalon.latestGeneralRating,
                    image = kapsalon.image
                )
            )
        }
        return kapsalonsListRoom
    }
}