package com.dubproductions.pokedex.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import coil.load
import com.dubproductions.pokedex.R
import com.dubproductions.pokedex.databinding.FragmentPokemonImageDialogBinding


class PokemonImageDialog : DialogFragment() {

    private lateinit var _binding: FragmentPokemonImageDialogBinding
    private val binding get() = _binding

    companion object {
        const val NATIONALNUM = "nationalNum"
    }

    private lateinit var pokemonNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pokemonNumber = it.getString(NATIONALNUM).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPokemonImageDialogBinding.inflate(inflater, container, false)

        binding.largerPokemonPicture.load(
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemonNumber}.png"
        ) {
            placeholder(R.drawable.pokeball)
            crossfade(700)
            error(R.drawable.pokeball)
        }

        return binding.root
    }

}
