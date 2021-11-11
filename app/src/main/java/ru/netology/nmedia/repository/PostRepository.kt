package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {

    fun likeById(id: Long)

    fun shareById(id: Long)

    fun getAll(): LiveData<List<Post>>

    fun removeById(id: Long)

    fun save(post: Post)

}