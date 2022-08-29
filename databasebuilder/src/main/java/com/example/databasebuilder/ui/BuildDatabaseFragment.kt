package com.example.databasebuilder.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.databasebuilder.DatabaseApplication
import com.example.databasebuilder.R
import com.example.databasebuilder.databinding.FragmentBuildDatabaseBinding
import com.example.databasebuilder.viewmodel.DatabaseViewModel
import com.example.databasebuilder.viewmodel.DatabaseViewModelFactory


class BuildDatabaseFragment : Fragment() {

    private val viewModel: DatabaseViewModel by activityViewModels {
        DatabaseViewModelFactory(
            (activity?.application as DatabaseApplication).database.databaseDao()
        )
    }

    private lateinit var _binding: FragmentBuildDatabaseBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.downloadData.setOnClickListener {
            viewModel.startRetrieval()
        }

        binding.addEvolutions.setOnClickListener {
            viewModel.initializeChainCount()
        }

        // Inflate the layout for this fragment
        _binding = FragmentBuildDatabaseBinding.inflate(inflater, container, false)
        return binding.root
    }

}
