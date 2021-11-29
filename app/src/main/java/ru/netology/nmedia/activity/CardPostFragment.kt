package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.os.bundleOf
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

    val post: Post = Post(
        id = 0,
        author = " ",
        date = " ",
        content = " ",
        likesCount = 0,
        likedByMe = false,
        shareCount = 0,
        video = null
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

       arguments?.showPost.let {
            with(binding) {
                authorName.text = it?.author
                date.text = it?.date
                contentPost.text = it?.content
                likes.text = PostService.countPresents(it!!.likesCount)
                share.text = PostService.countPresents(it.shareCount)
                videoLink.text = it.video
                likes.isChecked = it.likedByMe

                if (!it.video.isNullOrEmpty()) {
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
                                    viewModel.removeById(it.id.toLong())
                                    findNavController().navigateUp()
                                    true
                                }
                                R.id.edit -> {
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
                    viewModel.likeById(it.id.toLong())
                }
                share.setOnClickListener {
                    viewModel.shareById(it.id.toLong())
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