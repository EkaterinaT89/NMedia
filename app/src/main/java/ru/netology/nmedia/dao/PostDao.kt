package ru.netology.nmedia.dao

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostDao {

    fun likeById(id: Long)

    fun shareById(id: Long)

    fun getAll(): List<Post>

    fun removeById(id: Long)

    fun save(post: Post): Post

    fun video()
}