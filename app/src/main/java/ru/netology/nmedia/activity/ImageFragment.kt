package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.databinding.FragmentImageBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.service.imageLoad
import ru.netology.nmedia.util.PostArg
import ru.netology.nmedia.viewmodel.PostViewModel

@AndroidEntryPoint
class ImageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentImageBinding.inflate(
            inflater,
            container,
            false
        )

        with(binding) {

            arguments?.textArg.let {
                showImage.imageLoad("${BuildConfig.BASE_URL}/media/$it")
            }

            backButton.setOnClickListener {
                findNavController().navigate(R.id.feedFragment)
            }

        }


        return binding.root
    }


}

