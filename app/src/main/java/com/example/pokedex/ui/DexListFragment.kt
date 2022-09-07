package com.example.pokedex.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.PokedexApplication
import com.example.pokedex.R
import com.example.pokedex.adapter.DexAdapter
import com.example.pokedex.databinding.FragmentDexListBinding
import com.example.pokedex.viewmodel.DexViewModel
import com.example.pokedex.viewmodel.DexViewModelFactory


class DexListFragment : androidx.fragment.app.Fragment() {

    private val sharedViewModel: DexViewModel by activityViewModels {
        DexViewModelFactory(
            (activity?.application as PokedexApplication).database.pokemonDao()
        )
    }

    private lateinit var searchView: SearchView

    private val navListener = NavController.OnDestinationChangedListener { _, _, _ ->
        if (sharedViewModel.sortingData.value?.searchText != "") {
           sharedViewModel.sortingData.value?.temporarySearch = sharedViewModel.sortingData.value?.searchText.toString()
        }
        Log.i("search temp", "${sharedViewModel.sortingData.value?.temporarySearch}")
    }

    private lateinit var _binding: FragmentDexListBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDexListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        findNavController().removeOnDestinationChangedListener(navListener)
        searchView.setOnQueryTextListener(null)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu, menu)

                    val searchBar = menu.findItem(R.id.search)
                    searchView = searchBar.actionView as SearchView
                    searchView.queryHint = getString(R.string.search_hint)

                    searchView.setOnQueryTextListener(
                        object : SearchView.OnQueryTextListener {
                            override fun onQueryTextSubmit(query: String?): Boolean {
                                hideKeyboard()
                                return false
                            }

                            override fun onQueryTextChange(newText: String): Boolean {

                                Log.i("search string", newText)
                                if (!searchView.isIconified) {
                                    sharedViewModel.searchPokemon(newText)
                                    Log.i("search tried", "I tried it")
                                }

                                if (sharedViewModel.pokemonEntities.value?.size == 898) {
                                    sharedViewModel.sortingData.value?.temporarySearch = ""
                                }

                                Log.i("Search Text", "${sharedViewModel.sortingData.value?.searchText}")
                                return false
                            }
                        }
                    )

                    // Sets the search query text based off of saved search text.
                    if (sharedViewModel.sortingData.value?.temporarySearch != "") {
                        searchBar.expandActionView()
                        searchView.setQuery(sharedViewModel.sortingData.value?.temporarySearch, false)
//                        sharedViewModel.sortingData.value?.temporarySearch = ""

                    }
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    when (menuItem.itemId) {
                        R.id.sort -> {
                            findNavController().navigate(R.id.action_dexListFragment_to_sortingOptionsDialog)
                        }
                    }
                    return true
                }

            },
            viewLifecycleOwner,
            Lifecycle.State.CREATED
        )

        val recyclerView = view.findViewById<RecyclerView>(R.id.dex_recycler_view)

        sharedViewModel.pokemonEntities.observe(viewLifecycleOwner) { list ->
            recyclerView.adapter =
                sharedViewModel.sortingData.value?.sortBy?.let { sortBy -> DexAdapter(list, sortBy) }
        }

        findNavController().addOnDestinationChangedListener(navListener)

    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.menu, menu)
//
//        val searchBar = menu.findItem(R.id.search)
//        val searchView = searchBar.actionView as SearchView
//
//        searchView.queryHint = getString(R.string.search_hint)
//
//        if (sharedViewModel.sortingData.value?.temporarySearch != "") {
//            searchBar.expandActionView()
//            searchView.setQuery(sharedViewModel.sortingData.value?.temporarySearch, false)
//            sharedViewModel.sortingData.value?.temporarySearch?.let {
//                sharedViewModel.searchPokemon(it)
//            }
//            sharedViewModel.sortingData.value?.temporarySearch = ""
//        }
//
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                hideKeyboard()
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String): Boolean {
//
//                sharedViewModel.searchPokemon(newText)
//
//                return false
//            }
//
//        })
//
//        super.onCreateOptionsMenu(menu, inflater)
//    }


    // Hide keyboard functions
    private fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
////            R.id.download_data -> {
////                Toast.makeText(activity, R.string.downloading, Toast.LENGTH_LONG).show()
////                sharedViewModel.startRetrieval()
////            }
//
//            R.id.sort -> {
//                findNavController().navigate(R.id.action_dexListFragment_to_sortingOptionsDialog)
//            }
//
////            R.id.evolution_chains -> {
////                Toast.makeText(activity, getString(R.string.adding_evolutions), Toast.LENGTH_LONG).show()
////                sharedViewModel.initializeChainCount()
////            }
//
//
//        }
//
//        return super.onContextItemSelected(item)
//    }

}