package ru.netology.nmedia.repository

import android.view.View
import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.netology.nmedia.auth.AuthState
import ru.netology.nmedia.dto.Media
import ru.netology.nmedia.dto.MediaUpload
import ru.netology.nmedia.dto.Post

interface PostRepository {

    val data: Flow<PagingData<Post>>

    fun getNewerCount(id: Long): Flow<Int>

    suspend fun likeById(id: Long)

    suspend fun disLikeById(id: Long)

    fun shareById(id: Long)

    suspend fun removeById(id: Long)

    suspend fun save(post: Post)

    fun video()

    suspend fun getAll()

    suspend fun getUnreadPosts()

    suspend fun saveWithAttachment(post: Post, upload: MediaUpload)

    suspend fun upload(upload: MediaUpload): Media

    suspend fun signIn(login: String, pass: String)

    suspend fun signUp(name: String, login: String, pass: String)

}