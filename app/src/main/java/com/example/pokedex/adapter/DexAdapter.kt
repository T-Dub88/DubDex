package com.example.pokedex.adapter

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
import com.example.pokedex.data.SortingData.SortByEnum
import com.example.pokedex.ui.DexListFragmentDirections
import okhttp3.internal.format


class DexAdapter(
    private val data: List<Pokemon>,
    private val sortingInformation: SortByEnum
) : RecyclerView.Adapter<DexAdapter.ItemViewHolder> () {

    class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val pokemonName: TextView = view.findViewById(R.id.pokemon_name)
        val pokemonNumber: TextView = view.findViewById(R.id.pokemon_number)
        val pokemonPic: ImageView = view.findViewById(R.id.pokemon_image)
        val pokemonType1: ImageView = view.findViewById(R.id.type_1)
        val pokemonType2: ImageView = view.findViewById(R.id.type_2)
        val sortedStat: TextView = view.findViewById(R.id.sorted_stat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.dex_item, parent, false)

        return ItemViewHolder((adapterLayout))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = data[position]
        val resources = holder.itemView.context.resources
        val imageUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${item.nationalNum}.png"

        // Need a way to fetch types per item.
//        when (item.type1) {
//            "bug" -> holder.pokemonType1.setImageResource(R.drawable.bug)
//            "dark" -> holder.pokemonType1.setImageResource(R.drawable.dark)
//            "dragon" -> holder.pokemonType1.setImageResource(R.drawable.dragon)
//            "electric" -> holder.pokemonType1.setImageResource(R.drawable.electric)
//            "fairy" -> holder.pokemonType1.setImageResource(R.drawable.fairy)
//            "fighting" -> holder.pokemonType1.setImageResource(R.drawable.fighting)
//            "fire" -> holder.pokemonType1.setImageResource(R.drawable.fire)
//            "flying" -> holder.pokemonType1.setImageResource(R.drawable.flying)
//            "ghost" -> holder.pokemonType1.setImageResource(R.drawable.ghost)
//            "grass" -> holder.pokemonType1.setImageResource(R.drawable.grass)
//            "ground" -> holder.pokemonType1.setImageResource(R.drawable.ground)
//            "ice" -> holder.pokemonType1.setImageResource(R.drawable.ice)
//            "normal" -> holder.pokemonType1.setImageResource(R.drawable.normal)
//            "poison" -> holder.pokemonType1.setImageResource(R.drawable.poison)
//            "rock" -> holder.pokemonType1.setImageResource(R.drawable.rock)
//            "steel" -> holder.pokemonType1.setImageResource(R.drawable.steel)
//            "water" -> holder.pokemonType1.setImageResource(R.drawable.water)
//            "psychic" -> holder.pokemonType1.setImageResource(R.drawable.psychic)
//            null -> holder.pokemonType1.setImageResource(android.R.color.transparent)
//        }
//
//        when (item.type2) {
//            "bug" -> holder.pokemonType2.setImageResource(R.drawable.bug)
//            "dark" -> holder.pokemonType2.setImageResource(R.drawable.dark)
//            "dragon" -> holder.pokemonType2.setImageResource(R.drawable.dragon)
//            "electric" -> holder.pokemonType2.setImageResource(R.drawable.electric)
//            "fairy" -> holder.pokemonType2.setImageResource(R.drawable.fairy)
//            "fighting" -> holder.pokemonType2.setImageResource(R.drawable.fighting)
//            "fire" -> holder.pokemonType2.setImageResource(R.drawable.fire)
//            "flying" -> holder.pokemonType2.setImageResource(R.drawable.flying)
//            "ghost" -> holder.pokemonType2.setImageResource(R.drawable.ghost)
//            "grass" -> holder.pokemonType2.setImageResource(R.drawable.grass)
//            "ground" -> holder.pokemonType2.setImageResource(R.drawable.ground)
//            "ice" -> holder.pokemonType2.setImageResource(R.drawable.ice)
//            "normal" -> holder.pokemonType2.setImageResource(R.drawable.normal)
//            "poison" -> holder.pokemonType2.setImageResource(R.drawable.poison)
//            "rock" -> holder.pokemonType2.setImageResource(R.drawable.rock)
//            "steel" -> holder.pokemonType2.setImageResource(R.drawable.steel)
//            "water" -> holder.pokemonType2.setImageResource(R.drawable.water)
//            "psychic" -> holder.pokemonType2.setImageResource(R.drawable.psychic)
//            null -> holder.pokemonType2.setImageResource(android.R.color.transparent)
//        }

        fun ImageView.setDrawableName(type: String?) {
            setImageResource(
                type?.let {
                    resources.getIdentifier(it, "drawable", context?.packageName)
                } ?: android.R.color.transparent
            )
        }

        holder.pokemonType1.setDrawableName(item.type1)
        holder.pokemonType2.setDrawableName(item.type2)

        // Puts stat on card when sorted.
        when (sortingInformation) {
            SortByEnum.HP_STAT -> holder.sortedStat.text =
                resources.getString(R.string.hp_stat_sort, item.hpStat)

            SortByEnum.ATTACK_STAT-> holder.sortedStat.text =
                resources.getString(R.string.attack_stat_short, item.attackStat)

            SortByEnum.DEFENSE_STAT -> holder.sortedStat.text =
                resources.getString(R.string.defense_stat_short, item.defenseStat)

            SortByEnum.SPECIAL_ATTACK_STAT -> holder.sortedStat.text =
                resources.getString(R.string.special_attack_stat_short, item.specialAttackStat)

            SortByEnum.SPECIAL_DEFENSE_STAT-> holder.sortedStat.text =
                resources.getString(R.string.special_defense_stat_short, item.specialDefenseStat)

            SortByEnum.SPEED_STAT -> holder.sortedStat.text =
                resources.getString(R.string.speed_stat_short, item.speedStat)

            SortByEnum.TOTAL_STATS -> holder.sortedStat.text =
                resources.getString(R.string.total_stats_short, item.totalStats)

            else -> holder.sortedStat.text = resources.getString(R.string.empty)
        }

        holder.pokemonNumber.text = format("#%03d", item.nationalNum)
        holder.pokemonName.text = item.pokemonName

        holder.pokemonPic.load(imageUrl) {
            placeholder(R.drawable.pokeball)
            crossfade(700)
            error(R.drawable.pokeball)
        }

        holder.itemView.setOnClickListener {
            Log.i("clicked", "clicked")
            val action = DexListFragmentDirections
                .actionDexListFragmentToPokemonInfoFragment(
                    pokemonPlacement = item.nationalNum.toString(),
                    evolutionNav = false
                )
            holder.view.findNavController().navigate(action)
        }

    }

    override fun getItemCount(): Int = data.size

}