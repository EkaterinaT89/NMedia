package ru.netology.nmedia

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel: PostViewModel by viewModels()
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.get().observe(this@MainActivity) { post ->
            with(binding) {
                authorName.text = post.author
                date.text = post.date
                contentPost.text = post.content
                likesCount.text = PostService.countPresents(post.likesCount)
                shareCount.text = PostService.countPresents(post.shareCount)
                likes.setImageResource(
                    if (post.likedByMe) R.drawable.liked else R.drawable.ic_baseline_favorite_border_24
                )
            }

            binding.likes.setOnClickListener {
                viewModel.Like()
            }

            binding.share.setOnClickListener {
                viewModel.share()
            }

        }

    }

}

