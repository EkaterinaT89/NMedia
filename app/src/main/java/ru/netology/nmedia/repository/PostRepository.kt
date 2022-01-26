package ru.netology.nmedia.repository

import android.view.View
import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {

    fun likeById(id: Long)

    fun disLikeById(id: Long)

    fun shareById(id: Long)

    fun getAll(): List<Post>

    fun removeById(id: Long)

    fun save(post: Post)

    fun video()

}