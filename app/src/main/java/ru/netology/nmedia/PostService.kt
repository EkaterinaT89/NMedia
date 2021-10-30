package ru.netology.nmedia

object PostService {

    val post = Post (id = 1, author = " ", content = " ", likedByMe=false,
       date = " ")

    fun countLikesPresents(post: Post): String {
       return when(post.likesCount) {
            in 0..999 -> "${post.likesCount}"
            in 1000..10000 -> "1K"
            in 10001..1000000 -> "${post.likesCount/100}" + "K"
            else -> "${post.likesCount/1000}" + "M"
        }
    }

    fun countSharePresents(post: Post): String {
        return when(post.shareCount) {
            in 0..999 -> "${post.shareCount}"
            in 1100..10000 -> "1K"
            in 10001..1000000 -> "${post.shareCount/100}" + "K"
            else -> "${post.shareCount/1000}" + "M"
        }
    }

}