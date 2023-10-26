package com.example.gifviewer.presentation.fragments.details

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.gifviewer.R
import com.example.gifviewer.data.network.model.GiphyResponse
import com.example.gifviewer.databinding.FragmentGifDetailsBinding
import com.example.gifviewer.presentation.fragments.GifViewModel
import com.example.gifviewer.presentation.fragments.base.BaseFragment
import com.example.gifviewer.presentation.fragments.home.GifAdapter
import com.example.gifviewer.util.collectOnLifecycleStart
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Response

@AndroidEntryPoint
class GifDetailsFragment : BaseFragment(R.layout.fragment_gif_details) {

    private val viewModel: GifViewModel by activityViewModels()
    private var binding: FragmentGifDetailsBinding? = null

    private var gifAdapter: GifAdapter? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGifDetailsBinding.bind(view).also {
            initViews(it)
            initAdapter(it)
        }
    }

    override fun initSubscriptions() {
        viewModel.result.collectOnLifecycleStart(viewLifecycleOwner) { response ->
            val data = getGifsList(response)
            gifAdapter?.submitList(data)

            initCurrentItem()
        }
    }

    private fun getGifsList(response: Response<GiphyResponse>): List<GifAdapter.AdapterItem.FullScreenGifItem>? {
        val gifData = response.body()?.gifData
        val images = gifData?.map { it.images.fixedHeight.url }
        return images?.map { GifAdapter.AdapterItem.FullScreenGifItem(it) }
    }

    private fun initAdapter(binding: FragmentGifDetailsBinding) {
        gifAdapter = GifAdapter {}
        binding.fullScreenRecycler.adapter = gifAdapter

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.fullScreenRecycler)
    }

    private fun initCurrentItem() {
        val position = viewModel.getSelectedPosition()
        binding?.fullScreenRecycler?.layoutManager?.scrollToPosition(position)
    }

    private fun initViews(binding: FragmentGifDetailsBinding) {
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.downloadButton.setOnClickListener {
            Toast.makeText(requireContext(), "Coming soon", Toast.LENGTH_SHORT).show()
        }
    }
}