package com.example.gifviewer.presentation.fragments.myGifs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.MenuRes
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.gifviewer.R
import com.example.gifviewer.databinding.FragmentHomeNoInternetBinding
import com.example.gifviewer.models.MyGif
import com.example.gifviewer.presentation.fragments.GifViewModel
import com.example.gifviewer.presentation.fragments.base.BaseFragment
import com.example.gifviewer.presentation.fragments.home.GifAdapter
import com.example.gifviewer.util.collectOnLifecycleStart
import com.example.gifviewer.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyGifsFragment : BaseFragment(R.layout.fragment_home_no_internet) {

    private val viewModel: GifViewModel by activityViewModels()
    private var binding: FragmentHomeNoInternetBinding? = null

    private var gifAdapter: GifAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeNoInternetBinding.bind(view).also {
            initViews(it)
            initAdapter(it)
        }
        viewModel.getMyGifsFromDB()
    }

    override fun initSubscriptions() {
        viewModel.gifsFlow.collectOnLifecycleStart(viewLifecycleOwner) { myGifs ->
            val data = myGifs?.let { getGifsList(it) }
            gifAdapter?.submitList(data)
        }
    }

    private fun getGifsList(urlList: List<MyGif>): List<GifAdapter.AdapterItem.GifItem> {
        return urlList.map { gif ->
            GifAdapter.AdapterItem.GifItem(gif)
        }
    }

    private fun initAdapter(binding: FragmentHomeNoInternetBinding) {

        gifAdapter = GifAdapter { position ->
            showMenu(binding.gifsRecyclerView[position], R.menu.popup_menu, position)
        }
        binding.gifsRecyclerView.adapter = gifAdapter
    }

    private fun initViews(binding: FragmentHomeNoInternetBinding) {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchSavedGifs(query ?: "")
                binding.search.clearFocus()
                binding.search.hideKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        binding.noInternetButton.setOnClickListener {
            findNavController().navigate(MyGifsFragmentDirections.goToHomeFragment())
        }
    }

    @SuppressLint("RestrictedApi")
    private fun showMenu(view: View, @MenuRes menuRes: Int, position: Int) {

        val popup = PopupMenu(requireContext(), view)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.delete_gif -> {
                    viewModel.deleteGif(position)
                    true
                }

                else -> false
            }
        }
        if (popup.menu is MenuBuilder) {
            val menuBuilder = popup.menu as MenuBuilder
            menuBuilder.setOptionalIconsVisible(true)
        }
        popup.show()
    }
}