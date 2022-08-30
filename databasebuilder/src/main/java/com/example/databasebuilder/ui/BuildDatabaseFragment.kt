package com.example.databasebuilder.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.databasebuilder.databinding.FragmentBuildDatabaseBinding
import com.example.databasebuilder.viewmodel.DatabaseViewModel

class BuildDatabaseFragment : Fragment() {

    private val viewModel: DatabaseViewModel by activityViewModels()

    private lateinit var _binding: FragmentBuildDatabaseBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        // Inflate the layout for this fragment
        _binding = FragmentBuildDatabaseBinding.inflate(inflater, container, false)

        binding.downloadData.setOnClickListener {
            viewModel.startRetrieval()
        }

        binding.addEvolutions.setOnClickListener {
            viewModel.initializeChainCount()
        }

        return binding.root
    }

}
