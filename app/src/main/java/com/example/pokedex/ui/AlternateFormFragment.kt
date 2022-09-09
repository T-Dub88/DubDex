package com.example.pokedex.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.pokedex.PokedexApplication
import com.example.pokedex.R
import com.example.pokedex.adapter.AlternateFormAdapter
import com.example.pokedex.adapter.EvolutionAdapter
import com.example.pokedex.databinding.FragmentAlternateFormBinding
import com.example.pokedex.viewmodel.DexViewModel
import com.example.pokedex.viewmodel.DexViewModelFactory
import okhttp3.internal.format
import kotlin.properties.Delegates

class AlternateFormFragment : Fragment() {

    // Sets up binding for accessing views
    private lateinit var _binding: FragmentAlternateFormBinding
    private val binding get() = _binding

    // Sets up access to the view model and doa via the view model factory
    private val sharedViewModel: DexViewModel by activityViewModels {
        DexViewModelFactory(
            (activity?.application as PokedexApplication).database.pokemonDao()
        )
    }

    // Companion object for arguments passed through navigation
    companion object {
        const val POKEMONPLACEMENT = "pokemonPlacement"
        const val EVOLUTIONNAV = "evolutionNav"
    }

    // Reference to the variable that will be initialized from the nav.
    private lateinit var pokemonPlacement: String
    private var evolutionNav by Delegates.notNull<Boolean>()

    // Retrieves nav argument on creation
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
        // Inflate the layout for this fragment with binding.
        _binding = FragmentAlternateFormBinding.inflate(inflater, container, false)

        // Sets a reference to the current fragments pokemon.
        val currentPokemon = sharedViewModel.getAlternateEntity(pokemonPlacement.toInt())!!

        // Removes evolution card if pokemon doesn't evolve.
        sharedViewModel.evolutionList.observe(viewLifecycleOwner) {
            if (it.size <= 1) {
                binding.evolutionChainCard.visibility = GONE
            }

            else {
                binding.evolutionChainCard.visibility = VISIBLE
            }
        }

        // Hides ability views if abilities are missing.
        if (currentPokemon.ability2 == null) {
            binding.or.visibility = GONE
        }

        if (currentPokemon.hiddenAbility == null) {
            binding.hiddenAbilityHeader.visibility = GONE
            binding.hiddenAbility.visibility = GONE
            binding.abilitiesDivider.visibility = INVISIBLE
        }

        // Retrieves image of pokemon.
        // If no image can be found, uses a pokeball.
        binding.pokemonImage.load(
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${currentPokemon.id}.png"
        ) {
            placeholder(R.drawable.pokeball)
            crossfade(700)
            error(R.drawable.pokeball)
        }

        // Bindings for header info.
        binding.pokemonName.text = currentPokemon.name
        binding.pokemonNumber.text = format("#%03d", currentPokemon.nationalNum)
        binding.genus.text = sharedViewModel.getGenus(currentPokemon.nationalNum)
        binding.height.text =
            getString(R.string.height, currentPokemon.height.toString())
        binding.weight.text =
            getString(R.string.weight, currentPokemon.weight.toString())

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

        // Retrieves drawable names for types and binds the image.
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

        // Get references to the recyclerviews for evolutions and alts.
        val recyclerView = view.findViewById<RecyclerView>(R.id.evolution_recycler_view)
        val recyclerViewAlternate = view.findViewById<RecyclerView>(R.id.alternate_forms_recycler_view)

        // Live data observers for recyclerviews.
        sharedViewModel.evolutionList.observe(viewLifecycleOwner) {
            recyclerView.adapter = EvolutionAdapter(it, false)
        }

        sharedViewModel.alternateFormList.observe(viewLifecycleOwner) {
            recyclerViewAlternate.adapter = AlternateFormAdapter(it, false)
        }
    }
}
