package com.example.pokedex.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.pokedex.R
import com.example.pokedex.data.Pokemon
import com.example.pokedex.ui.PokemonInfoFragmentDirections
import okhttp3.internal.format

class EvolutionAdapter(
    private val data: List<Pokemon>,
) : RecyclerView.Adapter<EvolutionAdapter.ItemViewHolder> () {

    class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val pokemonName: TextView = view.findViewById(R.id.pokemon_name)
        val pokemonNumber: TextView = view.findViewById(R.id.pokemon_number)
        val pokemonPic: ImageView = view.findViewById(R.id.pokemon_image)
        val pokemonType1: ImageView = view.findViewById(R.id.type_1)
        val pokemonType2: ImageView = view.findViewById(R.id.type_2)
        val evolutionDetail: TextView = view.findViewById(R.id.evolution_detail)
        val evolutionTrigger: TextView = view.findViewById(R.id.evolution_trigger)
        val evolvesFrom: TextView = view.findViewById(R.id.evolves_from)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.evolution_item, parent, false)

        return ItemViewHolder((adapterLayout))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = data[position]
        val resources = holder.itemView.context.resources
        val imageUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${item.nationalNum}.png"

        // Sets the string for all the possible evolution details.
        var evolutionDetails = resources.getString(R.string.empty)
        // Checks each detail and adds to the string.
        if (item.gender != null) {
            evolutionDetails += when (item.gender) {
                1 -> resources.getString(R.string.gender_female)
                2 -> resources.getString(R.string.gender_male)
                else -> resources.getString(R.string.genderless)
            }
        }

        if (item.heldItem != null) {
            evolutionDetails += resources.getString(R.string.held_item, item.heldItem)
        }

        if (item.item != null) {
            evolutionDetails += resources.getString(R.string.item, item.item)
        }

        if (item.knowMove != null) {
            evolutionDetails += resources.getString(R.string.know_move, item.knowMove)
        }

        if (item.knownMoveType != null) {
            evolutionDetails += resources.getString(R.string.know_move_type, item.knownMoveType)
        }

        if (item.location != null) {
            evolutionDetails += resources.getString(R.string.location, item.location)
        }

        if (item.minAffection != null) {
            evolutionDetails += resources.getString(R.string.min_affection, item.minAffection)
        }

        if (item.minBeauty != null) {
            evolutionDetails += resources.getString(R.string.min_beauty, item.minBeauty)
        }

        if (item.minHappiness != null) {
            evolutionDetails += resources.getString(R.string.min_happiness, item.minHappiness)
        }

        if (item.minLevel != null) {
            evolutionDetails += resources.getString(R.string.min_level, item.minLevel)
        }

        if (item.needsOverworldRain == true) {
            evolutionDetails += resources.getString(R.string.needs_rain)
        }

        if (item.partySpecies != null) {
            evolutionDetails += resources.getString(R.string.party_species, item.partySpecies)
        }

        if (item.partyType != null) {
            evolutionDetails += resources.getString(R.string.party_type, item.partyType)
        }

        if (item.relativePhysicalStats != null) {
            evolutionDetails += when (item.relativePhysicalStats) {
                1 -> resources.getString(R.string.attack_greater_defense)
                0 -> resources.getString(R.string.attack_equal_defense)
                else -> resources.getString(R.string.attack_less_defense)
            }
        }

        if (item.timeOfDay != "") {
            evolutionDetails += resources.getString(R.string.time, item.timeOfDay)
        }

        if (item.tradeSpecies != null) {
            evolutionDetails += resources.getString(R.string.trade, item.tradeSpecies)
        }

        if (item.turnUpsideDown == true) {
            evolutionDetails += resources.getString(R.string.upside_down)
        }

        evolutionDetails = evolutionDetails.trim()

        fun ImageView.setDrawableName(type: String?) {
            setImageResource(
                type?.let {
                    resources.getIdentifier(it, "drawable", context?.packageName)
                } ?: android.R.color.transparent
            )
        }

        holder.pokemonType1.setDrawableName(item.type1)
        holder.pokemonType2.setDrawableName(item.type2)
        holder.pokemonNumber.text = format("#%03d", item.nationalNum)
        holder.pokemonName.text = item.pokemonName
        holder.evolvesFrom.text = if (item.evolvesFrom != null) {
            resources.getString(R.string.evolves_from, item.evolvesFrom)
        } else if (item.isBaby) {
            resources.getString(R.string.baby) + resources.getString(R.string.no_evolution)
        } else {
            resources.getString(R.string.no_evolution)
        }

//        holder.evolutionDetail.text = item.evolutionDetails
        holder.evolutionDetail.text = evolutionDetails
        holder.evolutionTrigger.text = item.evolutionTrigger

        holder.pokemonPic.load(imageUrl) {
            placeholder(R.drawable.pokeball)
            crossfade(700)
            error(R.drawable.pokeball)
        }

        holder.itemView.setOnClickListener {
            val action = PokemonInfoFragmentDirections
                .actionPokemonInfoFragmentSelf(
                    pokemonPlacement = item.nationalNum.toString(),
                    evolutionNav = true
                )
            holder.view.findNavController().navigate(action)
        }

    }

    override fun getItemCount(): Int = data.size


}