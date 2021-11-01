package ru.netology.nmedia.dto

data class Post(
    val id: Int,
    val author: String,
    val content: String,
    var likedByMe: Boolean,
    var likesCount: Long = 1999,
    val date: String,
    var shareCount: Long = 2000

) {

}