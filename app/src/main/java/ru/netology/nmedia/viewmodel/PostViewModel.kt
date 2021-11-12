package ru.netology.nmedia.viewmodel

import android.opengl.Visibility
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.R
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl

private val emptyPost = Post(
    id = 0,
    author = " ",
    date = " ",
    content = " ",
    likesCount = 0,
    likedByMe = false,
    shareCount = 0
)

class PostViewModel : ViewModel() {

    private val repository: PostRepository = PostRepositoryImpl()

    val edited = MutableLiveData(emptyPost)

    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = emptyPost
    }

    fun likeById(id: Long) {
        repository.likeById(id)
    }

    fun shareById(id: Long) {
        repository.shareById(id)
    }

    val data = repository.getAll()

    fun removeById(id: Long) = repository.removeById(id)

    fun edit(post: Post) {
        edited.value = post
    }

    fun editContent(text: String) {
        val formatted = text.trim()
        if(edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = formatted)
    }

}