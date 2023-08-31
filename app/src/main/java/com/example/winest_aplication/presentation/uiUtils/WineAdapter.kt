package com.example.winest_aplication.presentation.uiUtils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.winest_aplication.R
import com.example.winest_aplication.data.model.WineObjects

class WineAdapter(
    private val wines: List<WineObjects.WineResponse>,
    private val favoritedWinesIds: List<Int>,
    private val onClickButton: (Int) -> Unit,
    private val onRemoveFavoriteClick: (Int) -> Unit
) : RecyclerView.Adapter<WineAdapter.WineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wine_card, parent, false)
        return WineViewHolder(view)
    }

    override fun onBindViewHolder(holder: WineViewHolder, position: Int) {
        val wine = wines[position]
        holder.title.text = wine.title
        holder.description.text = wine.description
        if (favoritedWinesIds.contains(wine.id)) {
            holder.favoriteButton.setImageResource(R.drawable.ic_favorite_filled)
            holder.favoriteButton.setOnClickListener {
                onRemoveFavoriteClick(wine.id)
                holder.favoriteButton.setImageResource(R.drawable.ic_favorite_empty)
            }
        } else {
            holder.favoriteButton.setOnClickListener {
                onClickButton(wine.id)
                holder.favoriteButton.setImageResource(R.drawable.ic_favorite_filled)
            }
        }
    }

    override fun getItemCount() = wines.size

    class WineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val description: TextView = view.findViewById(R.id.description)
        val favoriteButton: ImageView =
            view.findViewById(R.id.btn_favorite)
    }
}
