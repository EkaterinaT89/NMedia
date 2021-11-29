package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.PostService
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.EditPostFragment.Companion.textArg
import ru.netology.nmedia.databinding.FragmentCardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.PostArg
import ru.netology.nmedia.viewmodel.PostViewModel

class CardPostFragment : Fragment() {

    companion object {
        var Bundle.showPost: Post? by PostArg
    }

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCardPostBinding.inflate(
            inflater,
            container,
            false
        )

        arguments?.showPost?.let { post: Post ->
            with(binding) {
                authorName.text = post.author
                date.text = post.date
                contentPost.text = post.content
                likes.text = PostService.countPresents(post.likesCount)
                share.text = PostService.countPresents(post.shareCount)
                videoLink.text = post.video
                likes.isChecked = post.likedByMe

                if (!post.video.isNullOrEmpty()) {
                    groupForVideo.visibility = View.VISIBLE
                } else {
                    groupForVideo.visibility = View.GONE
                }

                menuButton.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.post_menu)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.remove -> {
                                    viewModel.removeById(post.id)
                                    findNavController().navigateUp()
                                    true
                                }
                                R.id.edit -> {
                                    viewModel.edit(post)
                                    findNavController().navigateUp()
                                    findNavController().navigate(
                                        R.id.editPostFragment,
                                        Bundle().apply {
                                            textArg = contentPost.text.toString()
                                        })
                                    true
                                }
                                else -> false
                            }
                        }
                    }.show()
                }
                likes.setOnClickListener {
                    viewModel.likeById(post.id)
                }
                share.setOnClickListener {
                    viewModel.shareById(post.id)
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, post.content)
                        type = "text/plain"
                    }
                    val shareIntent =
                        Intent.createChooser(intent, getString(R.string.share_intent))
                    startActivity(shareIntent)
                }

                playVideo.setOnClickListener {
                    viewModel.video()
                    val videoIntent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                    startActivity(videoIntent)
                }

            }
        }
        return binding.root
    }

}