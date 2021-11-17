package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import ru.netology.nmedia.PostService.post
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityEditPostBinding
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel

class EditPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEditPostBinding.inflate(layoutInflater)

        setContentView(binding.root)
        binding.editContent.requestFocus()
        val viewModel: PostViewModel by viewModels()

        binding.group.visibility = View.VISIBLE
        binding.editContent.setText(intent?.getStringExtra(Intent.EXTRA_TEXT))

        binding.saveButton.setOnClickListener {
            val text = binding.editContent.text?.toString()

            if (text.isNullOrBlank()) {
                setResult(RESULT_CANCELED, intent)
                Toast.makeText(
                    this@EditPostActivity,
                    getString(R.string.Text_for_hint),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener

            } else {

                val intent = Intent()
                intent.putExtra(Intent.EXTRA_TEXT, text)
                setResult(Activity.RESULT_OK, intent)

                viewModel.editContent(text)
                viewModel.save()

                binding.editContent.setText(" ")
                binding.editContent.clearFocus()

                AndroidUtils.hideKeyboard(binding.editContent)
                binding.group.visibility = View.GONE
            }
            finish()
        }

        binding.cancelEditButton.setOnClickListener {
            with(binding.editContent) {
                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(this)
                binding.group.visibility = View.GONE
            }
            finish()
        }

    }
}

