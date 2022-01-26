package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.core.content.contentValuesOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.IOException
import java.util.Collections.copy
import kotlin.concurrent.thread

private val emptyPost = Post(
    id = 0L,
    author = " ",
    date = " ",
    content = " ",
    likesCount = 0,
    likedByMe = false,
    shareCount = 0,
    video = null
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryImpl()

    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data

    val edited = MutableLiveData(emptyPost)

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun save() {
        edited.value?.let {
            thread {
                repository.save(it)
                _postCreated.postValue(Unit)
            }
        }
        edited.value = emptyPost
    }

    fun likeById(id: Long) {
        thread {
            val old = _data.value?.posts.orEmpty()
            _data.postValue(
                _data.value?.copy(posts = _data.value?.posts.orEmpty()
                    .map {
                        it.copy(likesCount = it.likesCount + 1,
                        likedByMe = true)
                    })
            )
            try {
                repository.likeById(id)
            } catch (e: IOException) {
                _data.postValue(_data.value?.copy(posts = old))
            }
        }
    }

    fun disLikeById(id: Long) {
        thread {
            val old = _data.value?.posts.orEmpty()
            _data.postValue(
                _data.value?.copy(posts = _data.value?.posts.orEmpty()
                    .map {
                        it.copy(likesCount = it.likesCount - 1,
                            likedByMe = false)
                    })
            )
            try {
                repository.disLikeById(id)
            } catch (e: IOException) {
                _data.postValue(_data.value?.copy(posts = old))
            }
        }
    }

    fun shareById(id: Long) {
        thread {
            val old = _data.value?.posts.orEmpty()
            _data.postValue(
                _data.value?.copy(posts = _data.value?.posts.orEmpty()
                    .map {
                        it.copy(shareCount = it.shareCount + 1)
                    })
            )
            try {
                repository.shareById(id)
            } catch (e: IOException) {
                _data.postValue(_data.value?.copy(posts = old))
            }
        }
    }

    fun removeById(id: Long) {
        thread {
            val old = _data.value?.posts.orEmpty()
            _data.postValue(
                _data.value?.copy(posts = _data.value?.posts.orEmpty()
                    .filter { it.id != id }
                )
            )
            try {
                repository.removeById(id)
            } catch (e: IOException) {
                _data.postValue(_data.value?.copy(posts = old))
            }
        }
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun editContent(text: String) {
        val formatted = text.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = formatted)
    }

    fun video() {
        thread {
            repository.video()
        }
    }

    fun loadPosts() {
        thread {
            // Начало загрузки
            _data.postValue(FeedModel(loading = true))
            try {
                val posts = repository.getAll()
                FeedModel(posts = posts, empty = posts.isEmpty())
            } catch (e: IOException) {
                FeedModel(error = true)
            }.also(_data::postValue)
        }
    }
}