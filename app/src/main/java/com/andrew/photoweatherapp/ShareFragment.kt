package com.andrew.photoweatherapp

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.andrew.photoweatherapp.databinding.FragmentShareBinding
import com.andrew.photoweatherapp.presentation.FACEBOOK_PACKAGE
import com.andrew.photoweatherapp.presentation.TWITTER_PACKAGE
import com.andrew.photoweatherapp.presentation.shareImage

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
        drawFullPhoto()
        binding.facebookShareButton.setOnClickListener { shareImage(FACEBOOK_PACKAGE) }
        binding.twitterShareButton.setOnClickListener { shareImage(TWITTER_PACKAGE) }
    }

    private fun shareImage(packageId: String) = shareImage(requireContext(), path, packageId)

    private fun drawFullPhoto() = with(binding.fullPhotoImageView) {
        if (path.isBlank()) {
            scaleType = ImageView.ScaleType.CENTER
            setImageResource(R.drawable.ic_picture)
        } else {
            setImageURI(Uri.parse(path))
        }
    }

}
