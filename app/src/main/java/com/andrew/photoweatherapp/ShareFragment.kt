package com.andrew.photoweatherapp

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.andrew.photoweatherapp.databinding.FragmentShareBinding

class ShareFragment : Fragment() {
    private val path by lazy { ShareFragmentArgs.fromBundle(requireArguments()).uri }
    private lateinit var binding: FragmentShareBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShareBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (path.isBlank()) {
            binding.fullPhotoImageView.scaleType = ImageView.ScaleType.CENTER
            binding.fullPhotoImageView.setImageResource(R.drawable.ic_picture)
        } else {
            binding.fullPhotoImageView.setImageURI(Uri.parse(path))
        }
    }

}
