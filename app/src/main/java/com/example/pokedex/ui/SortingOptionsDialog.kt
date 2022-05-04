package com.example.pokedex.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.pokedex.PokedexApplication
import com.example.pokedex.R
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
            "nationalNum" -> binding.orderChoices.check(R.id.national_num)
            "name" -> binding.orderChoices.check(R.id.alphabetical)
            "hpStat" -> binding.orderChoices.check(R.id.hp)
            "attackStat" -> binding.orderChoices.check(R.id.attack)
            "defenseStat" -> binding.orderChoices.check(R.id.defense)
            "specialAttackStat" -> binding.orderChoices.check(R.id.special_attack)
            "specialDefenseStat" -> binding.orderChoices.check(R.id.special_defense)
            "totalStats" -> binding.orderChoices.check(R.id.total)
            else -> binding.orderChoices.check(R.id.speed)
        }

//        val switch = binding.ascendingDescending
//        switch.isChecked = sharedViewModel.

        binding.ascendingDescending.isChecked = sharedViewModel.sortingData.value?.ascending ?: true

        binding.applySorting.setOnClickListener {
            val orderStat = when (binding.orderChoices.checkedRadioButtonId) {
                R.id.national_num -> "nationalNum"
                R.id.alphabetical -> "name"
                R.id.hp -> "hpStat"
                R.id.attack -> "attackStat"
                R.id.defense -> "defenseStat"
                R.id.special_attack -> "specialAttackStat"
                R.id.special_defense -> "specialDefenseStat"
                R.id.total -> "totalStats"
                else -> "speedStat"
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