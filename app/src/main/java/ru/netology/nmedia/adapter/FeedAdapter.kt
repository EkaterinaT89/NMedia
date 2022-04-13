package ru.netology.nmedia.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.PostService
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.CardPostFragment.Companion.showPost
import ru.netology.nmedia.activity.EditPostFragment.Companion.textArg
import ru.netology.nmedia.databinding.CardAdBinding
import ru.netology.nmedia.databinding.FragmentCardPostBinding
import ru.netology.nmedia.dto.Ad
import ru.netology.nmedia.dto.FeedItem
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.service.imageLoad
import ru.netology.nmedia.service.loadCircleCrop
import ru.netology.nmedia.util.PostArg

interface OnInteractionListener {
    fun onLike(post: Post)
    fun onEdit(post: Post)
    fun onRemove(post: Post)
    fun onShare(post: Post)
    fun onPlayVideo(post: Post)
    fun onSinglePost(post: Post)
    fun onFullScreenImage(post: Post)
    fun onAdClick(ad: Ad) {}
}

class FeedAdapter(private val onInteractionListener: OnInteractionListener) :
    PagingDataAdapter<FeedItem, RecyclerView.ViewHolder>(FeedItemDiffCallback()) {

    private val typeAd = 0
    private val typePost = 1

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Ad -> typeAd
            is Post -> typePost
            null -> throw IllegalArgumentException("unknown item type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            typeAd -> AdViewHolder(
                CardAdBinding.inflate(layoutInflater, parent, false),
                onInteractionListener
            )
            typePost -> PostViewHolder(
                FragmentCardPostBinding.inflate(layoutInflater, parent, false),
                onInteractionListener
            )

            else -> throw IllegalArgumentException("unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is Ad -> (holder as AdViewHolder).bind(item)
            is Post -> (holder as PostViewHolder).bind(item)
            null -> error("unknown view type: $item")
        }
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
            avatar.loadCircleCrop("${BuildConfig.BASE_URL}/avatars/${post.authorAvatar}")
            likes.text = PostService.countPresents(post.likesCount)
            share.text = PostService.countPresents(post.shareCount)
            videoLink.text = post.video
            likes.isChecked = post.likedByMe

            if (!post.video.isNullOrEmpty()) {
                groupForVideo.visibility = View.VISIBLE
            } else {
                groupForVideo.visibility = View.GONE
            }

            if (post.attachment == null) {
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

            menuButton.visibility = if (post.ownedByMe) View.VISIBLE else View.INVISIBLE

            menuButton.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.post_menu)
                    menu.setGroupVisible(R.id.owned, post.ownedByMe)
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

            attachments.setOnClickListener {
                onInteractionListener.onFullScreenImage(post)
            }

        }
    }
}

class AdViewHolder(
    private val binding: CardAdBinding,
    private val onInteractionListener: OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(ad: Ad) {
        binding.apply {
            image.imageLoad("${BuildConfig.BASE_URL}/media/${ad.image}")
            image.setOnClickListener {
                onInteractionListener.onAdClick(ad)
            }
        }
    }
}

class FeedItemDiffCallback : DiffUtil.ItemCallback<FeedItem>() {
    override fun areItemsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
        if (oldItem::class != newItem::class) {
            return false
        }

        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
        return oldItem == newItem
    }
}

