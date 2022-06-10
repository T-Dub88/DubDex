package com.example.pokedex.ui

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.pokedex.PokedexApplication
import com.example.pokedex.R
import com.example.pokedex.adapter.EvolutionAdapter
import com.example.pokedex.databinding.FragmentPokemonInfoBinding
import com.example.pokedex.viewmodel.DexViewModel
import com.example.pokedex.viewmodel.DexViewModelFactory
import okhttp3.internal.format
import kotlin.properties.Delegates

class PokemonInfoFragment : Fragment() {

    private lateinit var _binding: FragmentPokemonInfoBinding
    private val binding get() = _binding
    private val sharedViewModel: DexViewModel by activityViewModels {
        DexViewModelFactory(
            (activity?.application as PokedexApplication).database.pokemonDao()
        )
    }

    companion object {
        const val POKEMONPLACEMENT = "pokemonPlacement"
        const val EVOLUTIONNAV = "evolutionNav"
    }

    private lateinit var pokemonPlacement: String
    private var evolutionNav by Delegates.notNull<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pokemonPlacement = it.getString(POKEMONPLACEMENT).toString()
            evolutionNav = it.getBoolean(EVOLUTIONNAV)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPokemonInfoBinding.inflate(inflater, container, false)

        val currentPokemon = if (evolutionNav) {
            sharedViewModel.getEvolutionEntity(pokemonPlacement.toInt())!!
        } else {
            sharedViewModel.getPokemonEntity(pokemonPlacement.toInt())!!
        }

        sharedViewModel.getEvolutionChain(currentPokemon.evolutionChain)

        binding.pokemonImage.load(
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${currentPokemon.nationalNum}.png"
        ) {
            placeholder(R.drawable.pokeball)
            crossfade(700)
            error(R.drawable.pokeball)
        }

        // Header card bindings.
        binding.pokemonName.text = currentPokemon.pokemonName
        binding.pokemonNumber.text = format("#%03d", currentPokemon.nationalNum)
        binding.height.text =
            getString(R.string.height, currentPokemon.height.toString())
        binding.weight.text =
            getString(R.string.weight, currentPokemon.weight.toString())
        binding.genus.text = currentPokemon.genus

        // Description binding.
        binding.descriptionContent.text = currentPokemon.description

        // Abilities bindings.
        binding.ability1.text = currentPokemon.ability1
        binding.ability2.text = currentPokemon.ability2
        binding.hiddenAbility.text = currentPokemon.hiddenAbility

        // Base stats bindings.
        binding.hpStat.text =
            getString(R.string.hp_stat, currentPokemon.hpStat)
        binding.hpPercent.text =
            getString(R.string.stat_percent, sharedViewModel.getPercent(currentPokemon.hpStat, currentPokemon.totalStats))

        binding.attackStat.text =
            getString(R.string.attack_stat, currentPokemon.attackStat)
        binding.attackPercent.text =
            getString(R.string.stat_percent, sharedViewModel.getPercent(currentPokemon.attackStat, currentPokemon.totalStats))

        binding.defenseStat.text =
            getString(R.string.defense_stat, currentPokemon.defenseStat)
        binding.defensePercent.text =
            getString(R.string.stat_percent, sharedViewModel.getPercent(currentPokemon.defenseStat, currentPokemon.totalStats))

        binding.specialAttackStat.text =
            getString(R.string.special_attack_stat, currentPokemon.specialAttackStat)
        binding.specialAttackPercent.text =
            getString(R.string.stat_percent, sharedViewModel.getPercent(currentPokemon.specialAttackStat, currentPokemon.totalStats))

        binding.specialDefenseStat.text =
            getString(R.string.special_defense_stat, currentPokemon.specialDefenseStat)
        binding.specialDefensePercent.text =
            getString(R.string.stat_percent, sharedViewModel.getPercent(currentPokemon.specialDefenseStat, currentPokemon.totalStats))

        binding.speedStat.text =
            getString(R.string.speed_stat, currentPokemon.speedStat)
        binding.speedPercent.text =
           getString(R.string.stat_percent, sharedViewModel.getPercent(currentPokemon.speedStat, currentPokemon.totalStats))

        binding.totalStats.text =
            getString(R.string.total_stats, currentPokemon.totalStats)

        // context?.packageName == "com.example.pokedex"
//        binding.type1.setImageResource(
//            currentPokemon.type1?.let {
//                resources.getIdentifier(it, "drawable", context?.packageName)
//            } ?: android.R.color.transparent
//        )
//
//        binding.type2.setImageResource(
//            currentPokemon.type2?.let {
//                resources.getIdentifier(it, "drawable", context?.packageName)
//            } ?: android.R.color.transparent
//        )

        fun ImageView.setDrawableName(type: String?) {
            setImageResource(
                type?.let {
                    resources.getIdentifier(it, "drawable", context?.packageName)
                } ?: android.R.color.transparent
            )
        }

        binding.type1.setDrawableName(currentPokemon.type1)
        binding.type2.setDrawableName(currentPokemon.type2)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.evolution_recycler_view)

        sharedViewModel.evolutionList.observe(viewLifecycleOwner) {
            recyclerView.adapter = EvolutionAdapter(it)
        }
    }
}
