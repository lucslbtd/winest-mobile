package com.example.winest_aplication.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.winest_aplication.data.network.WineService
import com.example.winest_aplication.databinding.FragmentWinesBinding
import com.example.winest_aplication.presentation.uiUtils.WineAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class WinesFragment : Fragment() {

    private lateinit var binding: FragmentWinesBinding
    private val apiService: WineService by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWinesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        loadFeed()
    }

    private fun loadFeed() = with(binding) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = apiService.getWines(0, 20, "")
            if (response.isSuccessful) {
                val wines = response.body()!!
                withContext(Dispatchers.Main) {
                    recyclerView.adapter = WineAdapter(wines)
                }
            } else {
                Log.e("APIStatusFeed", "Error: ${response.code()}")
            }
        }
    }

}
