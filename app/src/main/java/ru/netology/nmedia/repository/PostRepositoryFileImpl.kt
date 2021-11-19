package ru.netology.nmedia.repository

import android.content.Context
import android.system.Os.close
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.FileNotFoundException
import ru.netology.nmedia.dto.Post

class PostRepositoryFileImpl(private val context: Context) : PostRepository {

    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val fileName = "posts.json"
    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts)

    init {
        val file = context.filesDir.resolve(fileName)
        try {
            if (file.exists()) {
                context.openFileInput(fileName).bufferedReader().use {
                    posts = gson.fromJson(it, type)
                    data.value = posts
                }
            } else {
                sync()
            }
        } catch (e: FileNotFoundException) {
            println("Файл поврежден!")
        }
    }

//    init {
//        val file = context.filesDir.resolve(fileName)
//        if(file.exists()) {
//            context.openFileInput(fileName).bufferedReader().use {
//                posts = gson.fromJson(it, type)
//                data.value = posts
//            }
//        } else {
//            sync()
//        }
//    }

    fun sync() {
        context.openFileOutput(fileName, Context.MODE_PRIVATE)
            .bufferedWriter()
            .use {
                it.write(gson.toJson(posts))
            }
    }

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) {
                it
            } else if (it.id == id && it.likedByMe) {
                it.copy(likesCount = it.likesCount - 1, likedByMe = !it.likedByMe)
            } else {
                it.copy(
                    likedByMe = !it.likedByMe,
                    likesCount = it.likesCount + 1
                )
            }
        }
        data.value = posts
        sync()
    }

    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(shareCount = it.shareCount + 1)
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
        sync()
    }

    override fun save(post: Post) {
        if (post.id == 0L) {
            posts = listOf(post.copy(id = posts.firstOrNull()?.id?.inc() ?: 0)) + posts
            data.value = posts
            sync()
            return
        }
        posts = posts.map {
            if (it.id == post.id) {
                it.copy(content = post.content)
            } else {
                it
            }
        }
        data.value = posts
        sync()
    }

    override fun video() {
        data.value = posts
    }

}