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

    var currentId: Long? = null

    var lastAction: ActionType? = null

    init {
        loadPosts()
    }

    fun tryAgain() {
        when (lastAction) {
            ActionType.LIKEBYID -> retryLikeById()
            ActionType.DISLIKEBYID -> retryDisLikeById()
            ActionType.SAVE -> retrySave()
            ActionType.LOADPOSTS -> retryLoadPosts()
            ActionType.REMOVEBYID -> retryRemoveById()
            else -> loadPosts()
        }
    }

    fun save() {
        lastAction = ActionType.SAVE
        edited.value?.let {
            repository.save(it, object : PostRepository.Callback<Post> {
                override fun onSuccess(posts: Post) {
                    _postCreated.value = Unit
                    lastAction = null
                }
                override fun onError(e: Exception) {
                    edited.value = emptyPost
                    _data.postValue(FeedModel(serverError = true))
                }
            })
        }
    }

    fun retrySave() {
        save()
    }

    fun likeById(id: Long) {
        lastAction = ActionType.LIKEBYID
        currentId = id
        repository.likeById(id, object : PostRepository.Callback<Post> {
            override fun onSuccess(posts: Post) {
                val updatedPosts = _data.value?.posts.orEmpty()
                    .map {
                        if (it.id == posts.id) {
                            posts
                        } else {
                            it
                        }
                    }
                val newData = _data.value?.copy(posts = updatedPosts)
                _data.postValue(newData)
                currentId = null
                lastAction = null
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true, serverError = true))
            }
        })
    }

    fun retryLikeById() {
        currentId?.let {
            likeById(it)
        }
    }

    fun disLikeById(id: Long) {
        currentId = id
        lastAction = ActionType.DISLIKEBYID
        repository.disLikeById(id, object : PostRepository.Callback<Post> {
            override fun onSuccess(posts: Post) {
                val updatedPosts = _data.value?.posts.orEmpty()
                    .map {
                        if (it.id == posts.id) {
                            posts
                        } else {
                            it
                        }
                    }
                val newData = _data.value?.copy(posts = updatedPosts)
                _data.postValue(newData)
                currentId = 0
                lastAction = null
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true, serverError = true))
            }
        })
    }

    fun retryDisLikeById() {
        currentId?.let {
            disLikeById(it)
        }
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
        currentId = id
        lastAction = ActionType.REMOVEBYID
        val old = _data.value?.posts.orEmpty()
        repository.removeById(id, object : PostRepository.Callback<Unit> {
            override fun onSuccess(posts: Unit) {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .filter { it.id != id }
                    )
                )
                currentId = 0
                lastAction = null
            }

            override fun onError(e: Exception) {
                _data.postValue(_data.value?.copy(posts = old))
                _data.postValue(FeedModel(serverError = true))
            }
        })
    }

    fun retryRemoveById() {
        currentId?.let {
            removeById(it)
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
        repository.video()
    }

    fun loadPosts() {
        lastAction = ActionType.LOADPOSTS
        _data.value = _data.value?.copy(loading = true)
        repository.getAllAsync(object : PostRepository.Callback<List<Post>> {
            override fun onSuccess(posts: List<Post>) {
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
                lastAction = null
            }

            override fun onError(e: Exception) {
                _data.value = FeedModel(error = true, serverError = true)
            }
        })
    }

    fun retryLoadPosts() {
        loadPosts()
    }
}

enum class ActionType {
    LIKEBYID,
    DISLIKEBYID,
    SAVE,
    REMOVEBYID,
    LOADPOSTS
}