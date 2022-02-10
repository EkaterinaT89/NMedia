package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.IOException
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
            repository.save(it, object : PostRepository.Callback<Post> {
                override fun onSuccess(posts: Post) {
                    _postCreated.value = Unit
                }

                override fun onError(e: Exception) {
                    edited.value = emptyPost
                }
            })
        }
    }

    fun likeById(id: Long) {
        repository.likeById(id, object : PostRepository.Callback<Post> {
            override fun onSuccess(posts: Post) {
                val updatedPosts = _data.value?.posts.orEmpty()
                    .map {
                        if(it.id == posts.id) {
                            posts
                        } else {
                            it
                        }
                    }
                val newData = _data.value?.copy(posts = updatedPosts)
                _data.postValue(newData)

//                _data.postValue(
//                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
//                        .map {
//                            it.copy(
//                                likesCount = it.likesCount + 1,
//                                likedByMe = true
//                            )
//                        })
//                )
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun disLikeById(id: Long) {
        repository.disLikeById(id, object : PostRepository.Callback<Post> {
            override fun onSuccess(posts: Post) {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .map {
                            it.copy(
                                likesCount = it.likesCount - 1,
                                likedByMe = true
                            )
                        })
                )
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun shareById(id: Long) {
        repository.shareById(id, object : PostRepository.Callback<Post> {
            override fun onSuccess(posts: Post) {

            }

            override fun onError(e: Exception) {

            }
        })
    }

    fun removeById(id: Long) {
        val old = _data.value?.posts.orEmpty()
        repository.removeById(id, object : PostRepository.Callback<Unit> {
            override fun onSuccess(posts: Unit) {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .filter { it.id != id }
                    )
                )
            }

            override fun onError(e: Exception) {
                _data.postValue(_data.value?.copy(posts = old))
            }
        })
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
            repository.video()
    }

    fun loadPosts() {
        _data.value = _data.value?.copy(loading = true)
        repository.getAllAsync(object : PostRepository.Callback<List<Post>> {
            override fun onSuccess(posts: List<Post>) {
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            }

            override fun onError(e: Exception) {
                _data.value = FeedModel(error = true)
            }
        })
    }
}