package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentCardPostBinding
import ru.netology.nmedia.databinding.FragmentEditPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.PostArg
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class EditPostFragment : Fragment() {
    companion object {
        var Bundle.textArg: String? by StringArg
    }

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEditPostBinding.inflate(
            inflater,
            container,
            false
        )

        binding.group.visibility = View.VISIBLE

        arguments?.textArg.let(binding.editContent::setText)

        with(binding) {
                saveButton.setOnClickListener {
                    viewModel.editContent(editContent.text.toString())
                    viewModel.save()
                    AndroidUtils.hideKeyboard(requireView())
                    binding.group.visibility = View.GONE
                    findNavController().navigateUp()
                }

                cancelEditButton.setOnClickListener {
                    with(binding.editContent) {
                        AndroidUtils.hideKeyboard(this)
                        binding.group.visibility = View.GONE
                        findNavController().navigateUp()
                    }
                }

            }

        return binding.root
    }

}

