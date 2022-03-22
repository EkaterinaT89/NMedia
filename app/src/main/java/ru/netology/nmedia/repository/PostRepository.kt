package ru.netology.nmedia.repository

import android.view.View
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.dto.Post

interface PostRepository {

    val data: Flow<List<Post>>

    fun getNewerCount(id: Long): Flow<Int>

    suspend fun likeById(id: Long)

    suspend fun disLikeById(id: Long)

    fun shareById(id: Long)

    suspend fun removeById(id: Long)

    suspend fun save(post: Post)

    fun video()

    suspend fun getAll()

    suspend fun getUnreadPosts()

}