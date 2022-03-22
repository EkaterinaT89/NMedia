package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.model.FeedModelState
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
    video = null,
    show = false
)

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository =
        PostRepositoryImpl(AppDb.getInstance(context = application).postDao())

    val data: LiveData<FeedModel> = repository.data
        .map(::FeedModel)
        .catch { e -> e.printStackTrace() }
        .asLiveData(Dispatchers.Default)

    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    val newerCount: LiveData<Int> = data.switchMap {
        repository.getNewerCount(it.posts.firstOrNull()?.id ?: 0L)
            .catch { e -> e.printStackTrace() }
            .asLiveData(Dispatchers.Default)
    }

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
            _postCreated.value = Unit
            viewModelScope.launch {
                try {
                    repository.save(it)
                    _dataState.value = FeedModelState()
                } catch (e: Exception) {
                    _dataState.value = FeedModelState(error = true)
                }
            }
        }
        edited.value = emptyPost
    }

    fun retrySave() {
        save()
    }

    fun likeById(id: Long) {
        viewModelScope.launch {
            currentId = id
            lastAction = ActionType.LIKEBYID
            try {
                repository.likeById(id)
                _dataState.value = FeedModelState()
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun retryLikeById() {
        currentId?.let {
            likeById(it)
        }
    }

    fun disLikeById(id: Long) {
        viewModelScope.launch {
            currentId = id
            lastAction = ActionType.DISLIKEBYID
            try {
                repository.disLikeById(id)
                _dataState.value = FeedModelState()
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun retryDisLikeById() {
        currentId?.let {
            disLikeById(it)
        }
    }

    fun shareById(id: Long) {
        repository.shareById(id)
    }

    fun removeById(id: Long) {
        viewModelScope.launch {
            currentId = id
            lastAction = ActionType.REMOVEBYID
            try {
                _dataState.value = FeedModelState()
                repository.removeById(id)
                _dataState.value = FeedModelState()
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
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

    fun loadPosts() = viewModelScope.launch {
        lastAction = ActionType.LOADPOSTS
        try {
            _dataState.value = FeedModelState(loading = true)
            repository.getAll()
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun refreshPosts() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(refreshing = true)
            repository.getAll()
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun retryLoadPosts() {
        loadPosts()
    }

    fun getUnreadPosts() =
        viewModelScope.launch {
            try {
                _dataState.value = FeedModelState(loading = true)
                repository.getUnreadPosts()
                _dataState.value = FeedModelState()
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }

}

enum class ActionType {
    LIKEBYID,
    DISLIKEBYID,
    SAVE,
    REMOVEBYID,
    LOADPOSTS
}