package com.example.pokedex.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    @SuppressLint("SetTextI18n")
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
        binding.height.text = "HEIGHT: ${currentPokemon.height} m"
        binding.weight.text = "WEIGHT: ${currentPokemon.weight} kg"
        binding.genus.text = currentPokemon.genus

        // Description binding.
        binding.descriptionContent.text = currentPokemon.description

        // Abilities bindings.
        binding.ability1.text = currentPokemon.ability1
        binding.ability2.text = currentPokemon.ability2
        binding.hiddenAbility.text = currentPokemon.hiddenAbility

        // Base stats bindings.
        binding.hpStat.text = "hp: ${currentPokemon.hpStat}"
        binding.hpPercent.text =
            "${sharedViewModel.getPercent(currentPokemon.hpStat, currentPokemon.totalStats)}%"

        binding.attackStat.text = "attack: ${currentPokemon.attackStat}"
        binding.attackPercent.text =
            "${sharedViewModel.getPercent(currentPokemon.attackStat, currentPokemon.totalStats)}%"

        binding.defenseStat.text = "defense: ${currentPokemon.defenseStat}"
        binding.defensePercent.text =
            "${sharedViewModel.getPercent(currentPokemon.defenseStat, currentPokemon.totalStats)}%"

        binding.specialAttackStat.text = "sp. attack: ${currentPokemon.specialAttackStat}"
        binding.specialAttackPercent.text =
            "${sharedViewModel.getPercent(currentPokemon.specialAttackStat, currentPokemon.totalStats)}%"

        binding.specialDefenseStat.text = "sp. defense: ${currentPokemon.specialDefenseStat}"
        binding.specialDefensePercent.text =
            "${sharedViewModel.getPercent(currentPokemon.specialDefenseStat, currentPokemon.totalStats)}%"

        binding.speedStat.text = "speed: ${currentPokemon.speedStat}"
        binding.speedPercent.text =
            "${sharedViewModel.getPercent(currentPokemon.speedStat, currentPokemon.totalStats)}%"

        binding.totalStats.text = "total:\n${currentPokemon.totalStats}"


        // Type selections.
        when (currentPokemon.type1) {
            "bug" -> binding.type1.setImageResource(R.drawable.bug)
            "dark" -> binding.type1.setImageResource(R.drawable.dark)
            "dragon" -> binding.type1.setImageResource(R.drawable.dragon)
            "electric" -> binding.type1.setImageResource(R.drawable.electric)
            "fairy" -> binding.type1.setImageResource(R.drawable.fairy)
            "fighting" -> binding.type1.setImageResource(R.drawable.fighting)
            "fire" -> binding.type1.setImageResource(R.drawable.fire)
            "flying" -> binding.type1.setImageResource(R.drawable.flying)
            "ghost" -> binding.type1.setImageResource(R.drawable.ghost)
            "grass" -> binding.type1.setImageResource(R.drawable.grass)
            "ground" -> binding.type1.setImageResource(R.drawable.ground)
            "ice" -> binding.type1.setImageResource(R.drawable.ice)
            "normal" -> binding.type1.setImageResource(R.drawable.normal)
            "poison" -> binding.type1.setImageResource(R.drawable.poison)
            "rock" -> binding.type1.setImageResource(R.drawable.rock)
            "steel" -> binding.type1.setImageResource(R.drawable.steel)
            "water" -> binding.type1.setImageResource(R.drawable.water)
            "psychic" -> binding.type1.setImageResource(R.drawable.psychic)
            null -> binding.type1.setImageResource(android.R.color.transparent)
        }

        when (currentPokemon.type2) {
            "bug" -> binding.type2.setImageResource(R.drawable.bug)
            "dark" -> binding.type2.setImageResource(R.drawable.dark)
            "dragon" -> binding.type2.setImageResource(R.drawable.dragon)
            "electric" -> binding.type2.setImageResource(R.drawable.electric)
            "fairy" -> binding.type2.setImageResource(R.drawable.fairy)
            "fighting" -> binding.type2.setImageResource(R.drawable.fighting)
            "fire" -> binding.type2.setImageResource(R.drawable.fire)
            "flying" -> binding.type2.setImageResource(R.drawable.flying)
            "ghost" -> binding.type2.setImageResource(R.drawable.ghost)
            "grass" -> binding.type2.setImageResource(R.drawable.grass)
            "ground" -> binding.type2.setImageResource(R.drawable.ground)
            "ice" -> binding.type2.setImageResource(R.drawable.ice)
            "normal" -> binding.type2.setImageResource(R.drawable.normal)
            "poison" -> binding.type2.setImageResource(R.drawable.poison)
            "rock" -> binding.type2.setImageResource(R.drawable.rock)
            "steel" -> binding.type2.setImageResource(R.drawable.steel)
            "water" -> binding.type2.setImageResource(R.drawable.water)
            "psychic" -> binding.type2.setImageResource(R.drawable.psychic)
            null -> binding.type2.setImageResource(android.R.color.transparent)
        }




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
