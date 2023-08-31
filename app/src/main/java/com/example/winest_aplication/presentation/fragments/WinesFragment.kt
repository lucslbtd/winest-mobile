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
        searchGPT()
    }

    private fun loadFeed() = with(binding) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getWines(0, 20, "")
                if (response.isSuccessful) {
                    val wines = response.body()!!
                    withContext(Dispatchers.Main) {
                        recyclerView.adapter = WineAdapter(wines)
                    }
                } else {
                    Log.e("APIStatusFeed", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("GRTPromptCatch", "Error: $e")
            }
        }
    }

    private fun searchGPT() = with(binding) {
        btnSearchWineSuggestion.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                pgPromptWines.visibility = View.VISIBLE
                try {
                    val gptResponse = apiService.gptPrompt()
                    if (gptResponse.isSuccessful) {
                        gptResponse.body()?.let { responseDialog(it.content) }
                    } else {
                        Log.e("GRTPrompt", "Error: ${gptResponse.code()}")
                    }
                } catch (e: Exception) {
                    Log.e("GRTPromptCatch", "Error: $e")
                } finally {
                    pgPromptWines.visibility = View.GONE
                }
            }
        }
    }

    private fun responseDialog(content: String) {
        val dialogView = layoutInflater.inflate(R.layout.item_dialog_wines, null)

        val dialogTextView = dialogView.findViewById<TextView>(R.id.tv_prompt_response_wines)

        dialogTextView.text = content

        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setTitle("Wines recommendation")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }

        val dialog = dialogBuilder.create()
        dialog.show()
    }
}
