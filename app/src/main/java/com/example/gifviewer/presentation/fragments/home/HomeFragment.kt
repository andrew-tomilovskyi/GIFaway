package com.example.gifviewer.presentation.fragments.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.gifviewer.R
import com.example.gifviewer.data.network.model.GiphyResponse
import com.example.gifviewer.databinding.FragmentHomeBinding
import com.example.gifviewer.presentation.fragments.GifViewModel
import com.example.gifviewer.presentation.fragments.base.BaseFragment
import com.example.gifviewer.util.collectOnLifecycleStart
import com.example.gifviewer.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Response

@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private val viewModel: GifViewModel by activityViewModels()
    private var binding: FragmentHomeBinding? = null

    private var gifAdapter: GifAdapter? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view).also {
            initViews(it)
            initAdapter(it)
        }
    }

    override fun initSubscriptions() {
        viewModel.result.collectOnLifecycleStart(viewLifecycleOwner) { response ->
            val data = getGifsList(response)
            gifAdapter?.submitList(data)
        }
    }

    private fun getGifsList(response: Response<GiphyResponse>): List<GifAdapter.AdapterItem.GifItem>? {
        val gifData = response.body()?.gifData
        val images = gifData?.map { it.images.fixedHeight.url }
        return images?.map { GifAdapter.AdapterItem.GifItem(it) }
    }

    private fun initViews(binding: FragmentHomeBinding) {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setQuery(query ?: "")
                binding.search.clearFocus()
                binding.search.hideKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun initAdapter(binding: FragmentHomeBinding) {
        gifAdapter = GifAdapter { position ->
            viewModel.setGifPosition(position)
            findNavController().navigate(HomeFragmentDirections.goToGifDetailsFragment())
        }
        binding.gifsRecyclerView.adapter = gifAdapter
    }
}