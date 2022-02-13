package ru.netology.nmedia.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.PostService
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.CardPostFragment.Companion.showPost
import ru.netology.nmedia.activity.EditPostFragment.Companion.textArg
import ru.netology.nmedia.databinding.FragmentCardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.PostArg


interface OnInteractionListener {
    fun onLike(post: Post)
    fun onEdit(post: Post)
    fun onRemove(post: Post)
    fun onShare(post: Post)
    fun onPlayVideo(post: Post)
    fun onSinglePost(post: Post)
}

class PostAdapter(private val onInteractionListener: OnInteractionListener) :
    ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding =
            FragmentCardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

}

class PostViewHolder(
    private val binding: FragmentCardPostBinding,
    private val onInteractionListener: OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        val url = "http://10.0.2.2:9999"

        binding.apply {
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

            if(post.attachment == null){
                attachments.visibility = View.GONE
            } else {
                attachments.visibility = View.VISIBLE

                Glide.with(attachments)
                    .load("$url/images/${post.attachment?.url}")
                    .error(R.drawable.ic_error)
                    .placeholder(R.drawable.ic_loading_avatar)
                    .timeout(10_000)
                    .into(attachments)
            }

            likes.setOnClickListener {
                onInteractionListener.onLike(post)
            }

            share.setOnClickListener {
                onInteractionListener.onShare(post)
            }

            playVideo.setOnClickListener {
                onInteractionListener.onPlayVideo(post)
            }

            contentPost.setOnClickListener {
                onInteractionListener.onSinglePost(post)
            }

            menuButton.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.post_menu)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }

            Glide.with(avatar)
                .load("$url/avatars/${post.authorAvatar}")
                .error(R.drawable.ic_error)
                .placeholder(R.drawable.ic_loading_avatar)
                .circleCrop()
                .timeout(10_000)
                .into(avatar)

        }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}