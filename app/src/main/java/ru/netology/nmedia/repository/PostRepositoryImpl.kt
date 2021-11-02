package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryImpl: PostRepository {

    val post: Post = Post(
        id = 1, author = "Новая нетология",
        content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по " +
                "онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению." +
                " Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. " +
                "Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, " +
                "которая заставляет хотеть больше, целиться выше, бежать быстрее. " +
                "Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
        date = "21 мая в 18:36", likedByMe = false)

    private val data = MutableLiveData(post)

    override fun like() {
        val currentPost = data.value?:return
        data.value = currentPost.copy(
            likedByMe = !currentPost.likedByMe,
            likesCount = if(currentPost.likedByMe) currentPost.likesCount -1 else currentPost.likesCount +1
        )
    }

    override fun share() {
        val currentPost = data.value?:return
        data.value = currentPost.copy(
            shareCount = currentPost.shareCount +1
        )
    }

    override fun get(): LiveData<Post> = data
}