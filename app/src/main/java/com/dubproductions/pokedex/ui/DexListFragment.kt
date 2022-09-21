package com.dubproductions.pokedex.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.dubproductions.pokedex.PokedexApplication
import com.dubproductions.pokedex.R
import com.dubproductions.pokedex.adapter.DexAdapter
import com.dubproductions.pokedex.databinding.FragmentDexListBinding
import com.dubproductions.pokedex.viewmodel.DexViewModel
import com.dubproductions.pokedex.viewmodel.DexViewModelFactory

class DexListFragment : Fragment() {

    private val sharedViewModel: DexViewModel by activityViewModels {
        DexViewModelFactory(
            (activity?.application as PokedexApplication).database.pokemonDao()
        )
    }

    private lateinit var searchView: SearchView

    private lateinit var _binding: FragmentDexListBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDexListBinding.inflate(inflater, container, false)
        return binding.root
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
                                return true
                            }

                            override fun onQueryTextChange(newText: String): Boolean {
                                if (!searchView.isIconified) {
                                    sharedViewModel.searchPokemon(newText)
                                }

                                return true
                            }
                        }
                    )
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    when (menuItem.itemId) {
                        R.id.sort -> {
                            findNavController().navigate(R.id.action_dexListFragment_to_sortingOptionsDialog)
                        }

                        R.id.about -> {
                            findNavController().navigate(R.id.action_dexListFragment_to_informationFragment)
                        }
                    }
                    return true
                }

            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )

        val recyclerView = view.findViewById<RecyclerView>(R.id.dex_recycler_view)

        sharedViewModel.pokemonEntities.observe(viewLifecycleOwner) { list ->
            recyclerView.adapter =
                sharedViewModel.sortingData.value?.sortBy?.let { sortBy -> DexAdapter(list, sortBy) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchView.setOnQueryTextListener(null)
    }

    // Hide keyboard functions
    private fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}
