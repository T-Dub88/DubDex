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
import com.example.pokedex.data.SortingData.SortByEnum
import com.example.pokedex.ui.DexListFragmentDirections
import okhttp3.internal.format


// Adapter class for the cards in the main Pokedex.
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

        // URL for Pokemon artwork
        val imageUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${item.nationalNum}.png"

        // Sets image resource for types based off a type string from the database
        fun ImageView.setDrawableName(type: String?) {
            setImageResource(
                type?.let {
                    resources.getIdentifier(it, "drawable", context?.packageName)
                } ?: android.R.color.transparent
            )
        }

        holder.pokemonType1.setDrawableName(item.type1)
        holder.pokemonType2.setDrawableName(item.type2)

        // Puts stat on card when sorted
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
