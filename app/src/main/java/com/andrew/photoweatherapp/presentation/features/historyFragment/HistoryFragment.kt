package com.andrew.photoweatherapp.presentation.features.historyFragment

import android.graphics.Color
import android.os.Bundle
import android.os.Environment.DIRECTORY_PICTURES
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.andrew.photoweatherapp.R
import com.andrew.photoweatherapp.databinding.FragmentHistoryBinding
import com.andrew.photoweatherapp.presentation.PHOTOS
import com.andrew.photoweatherapp.presentation.THUMBS
import com.andrew.photoweatherapp.presentation.hide
import com.andrew.photoweatherapp.presentation.show

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private val thumbAdapter by lazy {
        ThumbRecyclerAdapter {navigateToShare(it.thumbToPhoto())}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        drawToolBar()
        getSavedThumbs().let { if (it.isNullOrEmpty())drawEmptyState() else drawDataState(it) }
    }

    private fun drawToolBar() = binding.toolbar.run {
        setupWithNavController(findNavController())
        setTitleTextColor(Color.WHITE)
        this.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_left_arrow)
    }

    private fun getSavedThumbs() = context
        ?.getExternalFilesDir(DIRECTORY_PICTURES)
        ?.listFiles()
        ?.asSequence()
        ?.map { it.path }
        ?.filter { it.contains(THUMBS) }
        ?.toList()

    private fun drawEmptyState() = with(binding) {
        thumbRecyclerView.hide()
        emptyLayout.show()
    }

    private fun drawDataState(items:List<String>) = with(binding) {
        thumbRecyclerView.show()
        emptyLayout.hide()
        thumbRecyclerView.adapter = thumbAdapter
        thumbAdapter.submitList(items)
    }

    private fun navigateToShare(uri:String)=HistoryFragmentDirections
        .actionHistoryFragmentToShareFragment(uri)
        .let { findNavController().navigate(it) }

    private fun String.thumbToPhoto() = replace(THUMBS, PHOTOS)
}

