package org.vanmierlo.kapsamazing_app

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Looper
import android.os.Bundle
import android.os.Handler
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import org.vanmierlo.kapsamazing_app.databinding.ActivityMainBinding
import org.vanmierlo.kapsamazing_app.databinding.FragmentHomeBinding
import org.vanmierlo.kapsamazing_app.ui.PostClickHandler
import java.util.concurrent.Executors
//Changed private val to var
class KapsalonsAdapter(private val kapsalons: List<Kapsalon>,
//                       private val clickHandler: PostClickHandler
                       ) : RecyclerView.Adapter<KapsalonsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.kapsalon_row, parent, false)
        return ViewHolder(view)
//        return ViewHolder(FragmentHomeBinding.inflate(LayoutInflater.from(parent.context, parent, false)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val kapsalon = kapsalons[position]

        d("kapsalon", "kapsalon? ${kapsalon.name}")

        holder.kapsalonName.text = kapsalon.name
        holder.kapsalonRestaurant.text = kapsalon.restaurant
        holder.kapsalonRating.text = kapsalon.latestGeneralRating.toString() + "/5"
        holder.kapsalonPrice.text = "€" + kapsalon.price.toString()
        holder.kapsalonOptions.text = kapsalon.delivered.toString()

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        var image: Bitmap? = null
        executor.execute {
            val imageUrl = kapsalon.image

            try {
                val `in` = java.net.URL(imageUrl).openStream()
                image = BitmapFactory.decodeStream(`in`)

                handler.post {
                    holder.kapsalonImage.setImageBitmap(image)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun getItemCount() = kapsalons.size

    inner class ViewHolder(itemView: View,
//                           binding: ActivityMainBinding
    ): RecyclerView.ViewHolder(itemView), View.OnClickListener{

//        var event = itemView.setOnClickListener(View.OnClickListener() {
//            fun onClick(v: View){
//                var i: Intent = Intent(v.context, DetailActivity::class.java)
//                i.putExtra("details", Bundle())
//            }
//        })

//        init {
//            binding.root.setOnClickListener(this)
//        }

        val kapsalonName: TextView = itemView.findViewById(R.id.kapsalonName)
        val kapsalonRestaurant: TextView = itemView.findViewById(R.id.kapsalonRestaurant)
        val kapsalonImage: ImageView = itemView.findViewById(R.id.kapsalonImage)
        val kapsalonRating: TextView = itemView.findViewById(R.id.kapsalonRating)
        val kapsalonPrice: TextView = itemView.findViewById(R.id.kapsalonPrice)
        val kapsalonOptions: TextView = itemView.findViewById(R.id.kapsalonOptions)

        override fun onClick(p0: View?) {
            val position = adapterPosition
            val currentKapsalon = kapsalons[position]
        }
    }

//    private fun onClick(itemView: View){
//        val implicitIntent = Intent(itemView.context, DetailActivity::class.java)

        // add some data to the intent
//        implicitIntent.data = Uri.parse("https://web2-kapsamazing-driesv.herokuapp.com/kapsalon?id=123321")

        // start activity with the intent, the system will choose the appropriate app
        // or ask the user to choose if multiple apps are available
//        startActivity(Intent(this, DetailActivity::class.java))
//    }
}
