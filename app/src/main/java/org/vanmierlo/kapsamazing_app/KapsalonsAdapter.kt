package org.vanmierlo.kapsamazing_app

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Looper
import android.os.Bundle
import android.os.Handler
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.concurrent.Executors
//Changed private val to var
class KapsalonsAdapter(var kapsalons: List<Kapsalon>) : RecyclerView.Adapter<KapsalonsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.kapsalon_row, parent, false)
        return ViewHolder(view)
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

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val kapsalonName: TextView = itemView.findViewById(R.id.kapsalonName)
        val kapsalonRestaurant: TextView = itemView.findViewById(R.id.kapsalonRestaurant)
        val kapsalonImage: ImageView = itemView.findViewById(R.id.kapsalonImage)
        val kapsalonRating: TextView = itemView.findViewById(R.id.kapsalonRating)
        val kapsalonPrice: TextView = itemView.findViewById(R.id.kapsalonPrice)
        val kapsalonOptions: TextView = itemView.findViewById(R.id.kapsalonOptions)
    }
}
