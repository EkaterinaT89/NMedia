package ru.netology.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl

class PostViewModel: ViewModel() {

    private val repository:PostRepository = PostRepositoryImpl()

    fun Like() {
        repository.like()
    }

    fun share() {
        repository.share()
    }

    fun get(): LiveData<Post> = repository.get()

}