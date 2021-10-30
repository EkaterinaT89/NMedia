package ru.netology.nmedia

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ru.netology.nmedia.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post: Post = Post(
            id = 1, author = "Новая нетология",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по " +
                    "онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению." +
                    " Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. " +
                    "Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, " +
                    "которая заставляет хотеть больше, целиться выше, бежать быстрее. " +
                    "Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            date = "21 мая в 18:36", likedByMe = false)

        with(binding) {
            authorName.text = post.author
            date.text = post.date
            contentPost.text = post.content
            likesCount.text = post.likesCount.toString()
            shareCount.text = post.shareCount.toString()

            likes.setImageResource(
                if (post.likedByMe) {
                    R.drawable.liked
                } else {
                    R.drawable.ic_baseline_favorite_border_24
                }
            )

            likes.setOnClickListener {
                post.likedByMe = !post.likedByMe
                likes.setImageResource(
                    if (post.likedByMe) {
                        R.drawable.liked
                    } else {
                        R.drawable.ic_baseline_favorite_border_24
                    }
                )
                if (post.likedByMe) post.likesCount++
                else post.likesCount--
                likesCount.text = PostService.countLikesPresents(post)

            }

            share.setOnClickListener {
                post.shareCount++
                shareCount.text = PostService.countSharePresents(post)
            }

        }

    }

}

