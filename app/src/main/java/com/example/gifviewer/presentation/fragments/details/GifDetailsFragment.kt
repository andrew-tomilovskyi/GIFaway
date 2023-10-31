package com.example.gifviewer.presentation.fragments.details

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.gifviewer.R
import com.example.gifviewer.databinding.FragmentGifDetailsBinding
import com.example.gifviewer.models.MyGif
import com.example.gifviewer.presentation.fragments.GifViewModel
import com.example.gifviewer.presentation.fragments.base.BaseFragment
import com.example.gifviewer.presentation.fragments.home.GifAdapter
import com.example.gifviewer.util.collectOnLifecycleStart
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GifDetailsFragment : BaseFragment(R.layout.fragment_gif_details) {

    private val viewModel: GifViewModel by activityViewModels()
    private var binding: FragmentGifDetailsBinding? = null

    private var gifAdapter: GifAdapter? = null

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            // prevents from ClassCastException
            val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return

            val currentPosition = layoutManager.findFirstVisibleItemPosition()
            viewModel.setGifPosition(currentPosition)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGifDetailsBinding.bind(view).also {
            initListeners(it)
            initAdapter(it)
        }
    }

    override fun initSubscriptions() {
        viewModel.result.collectOnLifecycleStart(viewLifecycleOwner) { gifList ->
            val data = getGifsList(gifList)
            gifAdapter?.submitList(data)

            initCurrentItem()
        }
        viewModel.gifAlreadyDownloadedFlow.collectOnLifecycleStart(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                getString(R.string.toast_gif_already_downloaded),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getGifsList(urlList: List<MyGif>): List<GifAdapter.AdapterItem.FullScreenGifItem> {
        return urlList.map { gif ->
            GifAdapter.AdapterItem.FullScreenGifItem(gif)
        }
    }

    private fun initAdapter(binding: FragmentGifDetailsBinding) {
        gifAdapter = GifAdapter()

        binding.fullScreenRecycler.adapter = gifAdapter

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.fullScreenRecycler)

        binding.fullScreenRecycler.addOnScrollListener(scrollListener)
    }

    private fun initCurrentItem() {
        val position = viewModel.getSelectedPosition()
        scrollToPosition(position)
    }

    private fun initListeners(binding: FragmentGifDetailsBinding) {
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.downloadButton.setOnClickListener {
            Toast.makeText(requireContext(),
                getString(R.string.toast_downloading), Toast.LENGTH_SHORT).show()

            viewModel.downloadGif(requireContext())
        }
    }

    private fun scrollToPosition(position: Int) {
        binding?.fullScreenRecycler?.post {
            binding?.fullScreenRecycler?.smoothScrollToPosition(position)
        }
    }
}