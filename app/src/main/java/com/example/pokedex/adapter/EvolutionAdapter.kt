package com.example.pokedex.adapter

import android.annotation.SuppressLint
import android.util.Log
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
import com.example.pokedex.ui.DexListFragmentDirections
import com.example.pokedex.ui.PokemonInfoFragmentDirections
import okhttp3.internal.format
import org.w3c.dom.Text

// TODO: Set the width of each card to be about half the parent card.

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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = data[position]
        val imageUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${item.nationalNum}.png"

        // Need a way to fetch types per item.
        when (item.type1) {
            "bug" -> holder.pokemonType1.setImageResource(R.drawable.bug)
            "dark" -> holder.pokemonType1.setImageResource(R.drawable.dark)
            "dragon" -> holder.pokemonType1.setImageResource(R.drawable.dragon)
            "electric" -> holder.pokemonType1.setImageResource(R.drawable.electric)
            "fairy" -> holder.pokemonType1.setImageResource(R.drawable.fairy)
            "fighting" -> holder.pokemonType1.setImageResource(R.drawable.fighting)
            "fire" -> holder.pokemonType1.setImageResource(R.drawable.fire)
            "flying" -> holder.pokemonType1.setImageResource(R.drawable.flying)
            "ghost" -> holder.pokemonType1.setImageResource(R.drawable.ghost)
            "grass" -> holder.pokemonType1.setImageResource(R.drawable.grass)
            "ground" -> holder.pokemonType1.setImageResource(R.drawable.ground)
            "ice" -> holder.pokemonType1.setImageResource(R.drawable.ice)
            "normal" -> holder.pokemonType1.setImageResource(R.drawable.normal)
            "poison" -> holder.pokemonType1.setImageResource(R.drawable.poison)
            "rock" -> holder.pokemonType1.setImageResource(R.drawable.rock)
            "steel" -> holder.pokemonType1.setImageResource(R.drawable.steel)
            "water" -> holder.pokemonType1.setImageResource(R.drawable.water)
            "psychic" -> holder.pokemonType1.setImageResource(R.drawable.psychic)
            null -> holder.pokemonType1.setImageResource(android.R.color.transparent)
        }

        when (item.type2) {
            "bug" -> holder.pokemonType2.setImageResource(R.drawable.bug)
            "dark" -> holder.pokemonType2.setImageResource(R.drawable.dark)
            "dragon" -> holder.pokemonType2.setImageResource(R.drawable.dragon)
            "electric" -> holder.pokemonType2.setImageResource(R.drawable.electric)
            "fairy" -> holder.pokemonType2.setImageResource(R.drawable.fairy)
            "fighting" -> holder.pokemonType2.setImageResource(R.drawable.fighting)
            "fire" -> holder.pokemonType2.setImageResource(R.drawable.fire)
            "flying" -> holder.pokemonType2.setImageResource(R.drawable.flying)
            "ghost" -> holder.pokemonType2.setImageResource(R.drawable.ghost)
            "grass" -> holder.pokemonType2.setImageResource(R.drawable.grass)
            "ground" -> holder.pokemonType2.setImageResource(R.drawable.ground)
            "ice" -> holder.pokemonType2.setImageResource(R.drawable.ice)
            "normal" -> holder.pokemonType2.setImageResource(R.drawable.normal)
            "poison" -> holder.pokemonType2.setImageResource(R.drawable.poison)
            "rock" -> holder.pokemonType2.setImageResource(R.drawable.rock)
            "steel" -> holder.pokemonType2.setImageResource(R.drawable.steel)
            "water" -> holder.pokemonType2.setImageResource(R.drawable.water)
            "psychic" -> holder.pokemonType2.setImageResource(R.drawable.psychic)
            null -> holder.pokemonType2.setImageResource(android.R.color.transparent)
        }

        holder.pokemonNumber.text = format("#%03d", item.nationalNum)
        holder.pokemonName.text = item.pokemonName
        holder.evolvesFrom.text = if (item.evolvesFrom != null) {
            "Evolves From:\n${item.evolvesFrom}"
        } else if (item.isBaby) {
            "Baby Pokemon"
        } else {
            "Basic Pokemon"
        }

        holder.evolutionDetail.text = item.evolutionDetails
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