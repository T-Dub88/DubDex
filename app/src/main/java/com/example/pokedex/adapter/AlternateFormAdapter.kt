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
//        val recyclerView = holder.itemView.context.resources
        val imageUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${item.id}.png"

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
