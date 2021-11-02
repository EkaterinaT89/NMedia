package ru.netology.nmedia.dto

data class Post(
    val id: Int,
    val author: String,
    val content: String,
    val likedByMe: Boolean,
    val likesCount: Long = 6999,
    val date: String,
    var shareCount: Long = 7000

) {

}