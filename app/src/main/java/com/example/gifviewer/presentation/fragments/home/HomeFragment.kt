package com.example.gifviewer.presentation.fragments.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import com.example.gifviewer.R
import com.example.gifviewer.databinding.FragmentHomeBinding
import com.example.gifviewer.presentation.fragments.base.BaseFragment
import com.example.gifviewer.util.collectOnLifecycleStart
import com.example.gifviewer.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()
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
            val gifData = response.body()?.gifData
            val images = gifData?.map { it.images.fixedHeight.url }
            gifAdapter?.submitList(images)
        }
    }

    private fun initViews(binding: FragmentHomeBinding) {
//        binding.searchView.doAfterTextChanged {
//            viewModel.setQuery(it.toString())
//        }

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
        gifAdapter = GifAdapter {
            Toast.makeText(requireContext(), "Click lol", Toast.LENGTH_SHORT).show()
        }
        binding.gifsRecyclerView.adapter = gifAdapter
    }
}