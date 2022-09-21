package com.example.pokedex.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pokedex.MainActivity
import com.example.pokedex.R
import com.example.pokedex.databinding.FragmentInformationBinding

class InformationFragment : Fragment() {

    // Set up binding for accessing views
    private lateinit var _binding: FragmentInformationBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInformationBinding.inflate(inflater, container, false)

        return  binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).showUpButton()
    }

    override fun onPause() {
        super.onPause()
        (activity as MainActivity).hideUpButton()
    }

}
