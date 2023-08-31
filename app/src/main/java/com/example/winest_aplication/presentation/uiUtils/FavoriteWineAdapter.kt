package com.example.winest_aplication.presentation.uiUtils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.winest_aplication.R
import com.example.winest_aplication.data.model.WineObjects

class FavoriteWineAdapter(
    private val wines: List<WineObjects.FavoriteWines>,
    private val onClickButton: (Int) -> Unit
) : RecyclerView.Adapter<FavoriteWineAdapter.FavoriteWineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteWineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wine_card, parent, false)
        val favoriteButton: ImageView =
            view.findViewById(R.id.btn_favorite)
        favoriteButton.setImageResource(R.drawable.ic_favorite_filled)
        return FavoriteWineViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteWineViewHolder, position: Int) {
        val wine = wines[position]
        holder.title.text = wine.wine.title
        holder.description.text = wine.wine.description
        holder.favoriteButton.setOnClickListener {
            onClickButton(wine.wine.id)
            holder.favoriteButton.setImageResource(R.drawable.ic_favorite_empty)
        }
    }


    override fun getItemCount() = wines.size

    class FavoriteWineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val description: TextView = view.findViewById(R.id.description)
        val favoriteButton: ImageView =
            view.findViewById(R.id.btn_favorite)
    }
}
