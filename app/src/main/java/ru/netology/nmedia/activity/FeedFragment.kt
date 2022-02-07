package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.CardPostFragment.Companion.showPost
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class FeedFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentFeedBinding.inflate(
            inflater,
            container,
            false
        )

        val adapter = PostAdapter(object : OnInteractionListener {
            override fun onEdit(post: Post) {
                viewModel.edit(post)
                binding.okButton.visibility = View.GONE
                findNavController().navigate(
                    R.id.editPostFragment,
                    Bundle().apply {
                        textArg = post.content
                    })
            }

            override fun onLike(post: Post) {
                if(!post.likedByMe) {
                    viewModel.likeById(post.id)
                } else {
                    viewModel.disLikeById(post.id)
                }
            }

            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(intent, getString(R.string.share_intent))
                startActivity(shareIntent)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onPlayVideo(post: Post) {
                viewModel.video()
                val videoIntent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                startActivity(videoIntent)
            }

            override fun onSinglePost(post: Post) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_cardPostFragment2,
                    Bundle().apply
                    {
                        showPost = post
                    })
            }
        })

        binding.container.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner, { state ->
            adapter.submitList(state.posts)
            with (binding) {
                progress.isVisible = state.loading
                errorGroup.isVisible = state.error
                emptyText.isVisible = state.empty
            }
        })

        binding.retryButton.setOnClickListener {
            viewModel.loadPosts()
        }

        binding.okButton.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        binding.swiperefresh.setOnRefreshListener {
            viewModel.loadPosts()
        }

        return binding.root

    }

}






