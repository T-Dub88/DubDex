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
import com.example.pokedex.data.AlternateForm
import com.example.pokedex.ui.AlternateFormFragmentDirections
import com.example.pokedex.ui.PokemonInfoFragmentDirections


// Adapter class for alternate for become listed at the bottom of a Pokemon's page.
class AlternateFormAdapter(
    private val data: List<AlternateForm>,
    private val infoFragment: Boolean
) : RecyclerView.Adapter<AlternateFormAdapter.ItemViewHolder> () {

    class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val pokemonName: TextView = view.findViewById(R.id.pokemon_name)
        val pokemonPic: ImageView = view.findViewById(R.id.pokemon_image)
        val type1: ImageView = view.findViewById(R.id.type_1)
        val type2: ImageView = view.findViewById(R.id.type_2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.alternate_form_item, parent, false)

        return ItemViewHolder((adapterLayout))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = data[position]

        // URL for the official artwork of a pokemon
        val imageUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${item.id}.png"

        // Takes in a string and sets an image resource based off the string
        // If no matching resource exists, the resource is set to transparent.
        fun ImageView.setDrawableName(type: String?) {
            setImageResource(
                type?.let {
                    resources.getIdentifier(it, "drawable", context?.packageName)
                } ?: android.R.color.transparent
            )
        }

        holder.pokemonName.text = item.name
        holder.pokemonPic.load(imageUrl) {
            placeholder(R.drawable.pokeball)
            crossfade(700)
            error(R.drawable.pokeball)
        }
        holder.type1.setDrawableName(item.type1)
        holder.type2.setDrawableName(item.type2)

        // Click listener for the card
        // Uses a different Nav action based off the type of fragment navigating from
        holder.itemView.setOnClickListener {
            val action = when (infoFragment) {
                true -> PokemonInfoFragmentDirections
                    .actionPokemonInfoFragmentToAlternateFormFragment(
                        pokemonPlacement = item.id.toString(),
                        evolutionNav = true
                    )
                else -> AlternateFormFragmentDirections
                    .actionAlternateFormFragmentSelf(
                        pokemonPlacement = item.id.toString(),
                        evolutionNav = true
                    )
            }

            holder.view.findNavController().navigate(action)
        }

    }
}
