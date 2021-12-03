package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post

class PostRepositorySQLiteImpl(
    private val dao: PostDao
) : PostRepository{

    private val data = MutableLiveData<List<Post>>(emptyList())

    init {
        data.value = dao.getAll()
    }

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        dao.likeById(id)
        val posts = checkNotNull(data.value).map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                likesCount = if (it.likedByMe) it.likesCount -1 else it.likesCount +1
            )
        }
        data.value = posts
    }

    override fun shareById(id: Long) {
        dao.shareById(id)
        val posts = checkNotNull(data.value).map {
            if (it.id != id) it else it.copy(
                shareCount = it.shareCount +1
            )
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
        val posts = checkNotNull(data.value).filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
        val id = post.id
        val saved = dao.save(post)

        val currentPosts = checkNotNull(data.value)
        val newPosts = if(id == 0L) {
            listOf(saved) + currentPosts
        } else {
            currentPosts.map {
                if (it.id != id) it else saved
            }
        }
       data.value = newPosts
    }

    override fun video() {
        dao.video()
        val currentPosts = checkNotNull(data.value)
        data.value = currentPosts
    }
}