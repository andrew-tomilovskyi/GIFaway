package com.example.gifviewer.presentation.fragments.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.gifviewer.R
import com.example.gifviewer.databinding.FragmentHomeBinding
import com.example.gifviewer.models.MyGif
import com.example.gifviewer.presentation.fragments.GifViewModel
import com.example.gifviewer.presentation.fragments.base.BaseFragment
import com.example.gifviewer.util.collectOnLifecycleStart
import com.example.gifviewer.util.hideKeyboard
import com.example.gifviewer.util.isNetworkConnected
import dagger.hilt.android.AndroidEntryPoint

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
            checkInternet(it)
        }
    }

    override fun initSubscriptions() {
        viewModel.result.collectOnLifecycleStart(viewLifecycleOwner) { gifList ->
            val data = getGifsList(gifList)
            gifAdapter?.submitList(data)
        }
    }

    private fun checkInternet(binding: FragmentHomeBinding) : Boolean {
        val isConnected = requireContext().isNetworkConnected()
        binding.noInternetGroup.isVisible = isConnected.not()
        binding.containerNestedScroll.isVisible = isConnected
        return isConnected
    }

    private fun getGifsList(urlList: List<MyGif>): List<GifAdapter.AdapterItem.GifItem> {
        return urlList.map { gif ->
            GifAdapter.AdapterItem.GifItem(gif)
        }
    }

    private fun initViews(binding: FragmentHomeBinding) {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setQuery(query ?: "")
                binding.search.clearFocus()
                binding.search.hideKeyboard()
                scrollToTheTop(binding)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        binding.noInternetButton.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.goToMyGifsFragment())
        }
        binding.loadMoreButton.setOnClickListener {
            scrollToTheTop(binding)
            viewModel.loadMore()
        }
        binding.reloadButton.setOnClickListener {
            if (checkInternet(binding)) {
                viewModel.loadDefaultGifs()
            } else {
                Toast.makeText(requireContext(),
                    getString(R.string.toast_no_internet), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initAdapter(binding: FragmentHomeBinding) {
        gifAdapter = GifAdapter { position ->
            viewModel.setGifPosition(position)
            findNavController().navigate(HomeFragmentDirections.goToGifDetailsFragment())
        }
        binding.gifsRecyclerView.adapter = gifAdapter
    }

    private fun scrollToTheTop(binding: FragmentHomeBinding) =
        binding.containerNestedScroll.smoothScrollTo(0, 0)

}