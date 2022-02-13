package ru.netology.nmedia.repository

import android.view.View
import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {

    fun likeById(id: Long, callback: Callback<Post>)

    fun disLikeById(id: Long, callback: Callback<Post>)

    fun shareById(id: Long, callback: Callback<Post>)

    fun removeById(id: Long, callback: Callback<Unit>)

    fun save(post: Post, callback: Callback<Post>)

    fun video()

    fun getAllAsync(callback: Callback<List<Post>>)

    interface Callback<T> {
        fun onSuccess(posts: T) {}
        fun onError(e: Exception) {}
    }
}