package org.vanmierlo.kapsamazing_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.vanmierlo.kapsamazing_app.data.KapsalonRoom

class KapsalonRoomAdapter(var kapsalonList: List<KapsalonRoom>): RecyclerView.Adapter<KapsalonRoomAdapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val kapsalonImage: ImageView
        val kapsalonName: TextView
        val kapsalonRestaurant: TextView
        val kapsalonOptions: TextView
        val kapsalonRating: TextView
        val kapsalonPrice: TextView


        init {
            kapsalonName = view.findViewById(R.id.kapsalonName)
            kapsalonImage = view.findViewById(R.id.kapsalonImage)
            kapsalonRestaurant = view.findViewById(R.id.kapsalonRestaurant)
            kapsalonOptions = view.findViewById(R.id.kapsalonOptions)
            kapsalonRating = view.findViewById(R.id.kapsalonRating)
            kapsalonPrice = view.findViewById(R.id.kapsalonPrice)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.kapsalon_row, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = kapsalonList[position].name
        val city = kapsalonList[position].city
        val restaurant = kapsalonList[position].restaurant
        val type = kapsalonList[position].type
        val delivered = kapsalonList[position].delivered
        val price = kapsalonList[position].price
        val kapid = kapsalonList[position].kapid
        val latestGeneralRating = kapsalonList[position].latestGeneralRating
        val image = kapsalonList[position].image

        holder.kapsalonName.text = name
        holder.kapsalonRestaurant.text = restaurant
        holder.kapsalonOptions.text = delivered.toString()
        holder.kapsalonRating.text = latestGeneralRating.toString()
        holder.kapsalonPrice.text = price.toString()
    }

    override fun getItemCount(): Int {
        return kapsalonList.size
    }

}