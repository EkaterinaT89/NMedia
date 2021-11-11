package ru.netology.nmedia

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel: PostViewModel by viewModels()

        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostAdapter(object : OnInteractionListener {
            override fun onEdit(post: Post) {
              viewModel.edit(post)
            }

            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }
        })

        binding.container.adapter = adapter

        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        viewModel.edited.observe(this) {post ->
            if(post.id == 0L) {
                return@observe
            }
            with(binding.editContent){
                requestFocus()
                setText(post.content)
            }
        }

        with(binding) {
            saveButton.setOnClickListener{
                val text = editContent.text?.toString()
                if(text.isNullOrBlank()) {
                    Toast.makeText(this@MainActivity, getString(R.string.Text_for_hint), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                viewModel.editContent(text)
                viewModel.save()

                editContent.setText(" ")
                editContent.clearFocus()

                AndroidUtils.hideKeyboard(editContent)
                binding.group.visibility = View.GONE

            }
        }

        binding.editContent.setOnClickListener{
            binding.group.visibility = View.VISIBLE
        }

        binding.cancelEditButton.setOnClickListener {
            with(binding.editContent) {
                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(this)
                binding.group.visibility = View.GONE
            }
        }

    }

}




