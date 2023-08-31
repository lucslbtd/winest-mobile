package com.example.winest_aplication.presentation.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.winest_aplication.R
import com.example.winest_aplication.data.network.WineService
import com.example.winest_aplication.databinding.FragmentFavoriteWinesBinding
import com.example.winest_aplication.databinding.FragmentWinesBinding
import com.example.winest_aplication.presentation.uiUtils.FavoriteWineAdapter
import com.example.winest_aplication.presentation.uiUtils.WineAdapter
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject

class FavoriteWinesFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteWinesBinding
    private val apiService: WineService by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteWinesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        loadFeed()
    }

    private fun loadFeed() = with(binding) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getFavoriteWines()
                if (response.isSuccessful) {
                    val wines = response.body()!!.favoriteWines
                    withContext(Dispatchers.Main) {
                        recyclerView.adapter = FavoriteWineAdapter(wines) { wineId ->
                            makeRequestWithWineId(wineId)
                        }
                    }
                } else {
                    Log.e("APIStatusFeed", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("GRTPromptCatch", "Error: $e")
            }
        }
    }

    private fun makeRequestWithWineId(wineId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.deleteFromFavorites(wineId)
                if (response.isSuccessful) {
                    loadFeed()
                    Log.e("removido", "Ok")
                } else {
                    Log.e("removido", "Error ${response.body()}")
                }
            } catch (e: Exception) {
                Log.e("GRTPromptCatch", "Error: $e")
            }
        }
    }




}
