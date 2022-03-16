package ru.netology.nmedia.repository

import android.view.View
import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {

    val data: LiveData<List<Post>>

    suspend fun likeById(id: Long)

    suspend fun disLikeById(id: Long)

    fun shareById(id: Long)

    suspend fun removeById(id: Long)

    suspend fun save(post: Post)

    fun video()

    suspend fun getAll()

}