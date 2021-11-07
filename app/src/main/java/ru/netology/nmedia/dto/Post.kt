package ru.netology.nmedia.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val likedByMe: Boolean,
    var likesCount: Long = 0,
    val date: String,
    var shareCount: Long = 0

) {

}