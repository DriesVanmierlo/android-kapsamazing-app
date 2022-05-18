package org.vanmierlo.kapsamazing_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class KapsalonsAdapter(private val kapsalons: MutableList<Kapsalon>) : RecyclerView.Adapter<KapsalonsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.kapsalon_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.kapsalonName.text = kapsalons[position].name
    }

    override fun getItemCount() = kapsalons.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val kapsalonName: TextView = itemView.findViewById(R.id.kapsalonName)
    }
}
