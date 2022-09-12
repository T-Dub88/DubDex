package com.example.pokedex.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.pokedex.PokedexApplication
import com.example.pokedex.R
import com.example.pokedex.data.SortingData.SortByEnum
import com.example.pokedex.databinding.FragmentSortingOptionsDialogBinding
import com.example.pokedex.viewmodel.DexViewModel
import com.example.pokedex.viewmodel.DexViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SortingOptionsDialog : BottomSheetDialogFragment() {

    private val sharedViewModel: DexViewModel by activityViewModels {
        DexViewModelFactory(
            (activity?.application as PokedexApplication).database.pokemonDao()
        )
    }

    private lateinit var _binding: FragmentSortingOptionsDialogBinding
    private val binding get() = _binding

    override fun onStart() {
        super.onStart()
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSortingOptionsDialogBinding.inflate(inflater, container, false)

        when (sharedViewModel.sortingData.value?.sortBy) {
            SortByEnum.NATIONAL_NUM -> binding.orderChoices.check(R.id.national_num)
            SortByEnum.NAME -> binding.orderChoices.check(R.id.alphabetical)
            SortByEnum.HP_STAT -> binding.orderChoices.check(R.id.hp)
            SortByEnum.ATTACK_STAT -> binding.orderChoices.check(R.id.attack)
            SortByEnum.DEFENSE_STAT -> binding.orderChoices.check(R.id.defense)
            SortByEnum.SPECIAL_ATTACK_STAT -> binding.orderChoices.check(R.id.special_attack)
            SortByEnum.SPECIAL_DEFENSE_STAT -> binding.orderChoices.check(R.id.special_defense)
            SortByEnum.TOTAL_STATS -> binding.orderChoices.check(R.id.total)
            else -> binding.orderChoices.check(R.id.speed)
        }

        binding.ascendingDescending.isChecked = sharedViewModel.sortingData.value?.ascending ?: true

        binding.applySorting.setOnClickListener {
            val orderStat = when (binding.orderChoices.checkedRadioButtonId) {
                R.id.national_num -> SortByEnum.NATIONAL_NUM
                R.id.alphabetical -> SortByEnum.NAME
                R.id.hp -> SortByEnum.HP_STAT
                R.id.attack -> SortByEnum.ATTACK_STAT
                R.id.defense -> SortByEnum.DEFENSE_STAT
                R.id.special_attack -> SortByEnum.SPECIAL_ATTACK_STAT
                R.id.special_defense -> SortByEnum.SPECIAL_DEFENSE_STAT
                R.id.total -> SortByEnum.TOTAL_STATS
                else -> SortByEnum.SPEED_STAT
            }

            val ascending = when (binding.ascendingDescending.isChecked) {
                true -> true
                else -> false
            }

            sharedViewModel.setOrder(orderStat)
            sharedViewModel.orderDex(ascending)

            dismiss()
        }

        // Inflate the layout for this fragment
        return binding.root
    }
}
