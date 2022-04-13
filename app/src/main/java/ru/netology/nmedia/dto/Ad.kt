package ru.netology.nmedia.dto

data class Ad(
    override val id: Long,
    val url: String,
    val image: String,
) : FeedItem()