package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import java.util.Collections.list

class PostRepositoryImpl: PostRepository {

    private var posts = List(10) {
        Post(
            id = it.toLong(),
            author = "Новая нетология",
            content = "$it Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по " +
                    "онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению." +
                    " Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. " +
                    "Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, " +
                    "которая заставляет хотеть больше, целиться выше, бежать быстрее. " +
                    "Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            date = "21 мая в 18:36", likedByMe = false)
    }

    private val data = MutableLiveData(posts)

   override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        posts = posts.map{
            if (it.id != id) {
                it
            } else if (it.id == id && it.likedByMe) {
                it.copy(likesCount = it.likesCount - 1, likedByMe = !it.likedByMe)
            } else {
                it.copy(likedByMe = !it.likedByMe,
                    likesCount = it.likesCount + 1)
            }
        }
        data.value = posts
    }

    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(shareCount = it.shareCount +1)
        }
        data.value = posts
    }



}