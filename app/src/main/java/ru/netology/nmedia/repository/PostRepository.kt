package ru.netology.nmedia.repository

import android.view.View
import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {

    fun likeById(id: Long, callback: GetOneCallback)

    fun disLikeById(id: Long, callback: GetOneCallback)

    fun shareById(id: Long, callback: GetOneCallback)

    fun getAll(): List<Post>

    fun removeById(id: Long, callback: GetOneCallback)

    fun save(post: Post, callback: GetPostCallback)

    fun video()

    fun getAllAsync(callback: GetAllCallback)

    interface GetAllCallback {
        fun onSuccess(posts: List<Post>) {}
        fun onError(e: Exception) {}
    }

    interface GetOneCallback {
        fun onSuccess(id: Long) {}
        fun onError(e: Exception) {}
    }

    interface GetPostCallback {
        fun onSuccess(post: Post) {}
        fun onError(e: Exception) {}
    }
}