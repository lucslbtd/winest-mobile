package com.example.winest_aplication.presentation.uiUtils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.winest_aplication.R
import com.example.winest_aplication.data.model.WineObjects

class WineAdapter(private val wines: List<WineObjects.WineResponse>) : RecyclerView.Adapter<WineAdapter.WineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wine_card, parent, false)
        return WineViewHolder(view)
    }

    override fun onBindViewHolder(holder: WineViewHolder, position: Int) {
        val wine = wines[position]
        holder.title.text = wine.title
        holder.description.text = wine.description
        // Bind other properties here
    }

    override fun getItemCount() = wines.size

    class WineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val description: TextView = view.findViewById(R.id.description)
        // Add more views for other properties
    }
}
