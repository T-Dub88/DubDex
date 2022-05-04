package com.example.pokedex.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.PokedexApplication
import com.example.pokedex.R
import com.example.pokedex.adapter.ItemAdapter
import com.example.pokedex.databinding.FragmentDexListBinding
import com.example.pokedex.viewmodel.DexViewModel
import com.example.pokedex.viewmodel.DexViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class DexListFragment : androidx.fragment.app.Fragment() {

    private val sharedViewModel: DexViewModel by activityViewModels {
        DexViewModelFactory(
            (activity?.application as PokedexApplication).database.pokemonDao()
        )
    }

    private lateinit var _binding: FragmentDexListBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDexListBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val recyclerView = view.findViewById<RecyclerView>(R.id.dex_recycler_view)


        sharedViewModel.pokemonEntities.observe(viewLifecycleOwner) {
            recyclerView.adapter = ItemAdapter(it)

        }


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)

        val searchBar = menu.findItem(R.id.search)
        val searchView = searchBar.actionView as SearchView

        val hint = "Enter Name or Number"

        searchView.queryHint = hint
        searchView.maxWidth = Int.MAX_VALUE

        if (sharedViewModel.sortingData.value?.searchText != "") {
            searchView.setQuery(sharedViewModel.sortingData.value?.searchText, false)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                hideKeyboard()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                val searchInput = newText?.lowercase()

                sharedViewModel.searchPokemon(searchInput)

                Log.i("new", sharedViewModel.pokemonEntities.value.toString())

//                for (item in sharedViewModel.pokemonEntities.value!!) {
//                    if (item.pokemonName.lowercase() == searchText) {
//                        sharedViewModel.activeList.value.add(item)
//                    } else if (item.nationalNum.toString() == searchText) {
//                        sharedViewModel.activeList.value.add(item)
//                    }
//                }

                return false
            }

        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    // Hide keyboard functions
    private fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.download_data -> {
                Toast.makeText(activity, "Downloading...", Toast.LENGTH_LONG).show()
                sharedViewModel.startRetrieval()
            }

            R.id.sort -> {
                findNavController().navigate(R.id.action_dexListFragment_to_sortingOptionsDialog)
            }


        }

        return super.onOptionsItemSelected(item)
    }

}