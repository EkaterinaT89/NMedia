package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Attachment
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val content: String,
    val likedByMe: Boolean,
    var likesCount: Long,
    val date: String,
    var shareCount: Long,
    var video: String? = null
) {

    fun toDto(): Post = with(this) {
        Post(
            id = id,
            author = author,
            content = content,
            date = date,
            likesCount = likesCount,
            likedByMe = likedByMe,
            shareCount = shareCount,
            video = video
            )
    }

    companion object {
        fun fromDto(dto: Post): PostEntity = with(dto) {
            PostEntity(
                id = id,
                author = author,
                content = content,
                date = date,
                likesCount = likesCount,
                likedByMe = likedByMe,
                shareCount = shareCount,
                video = video
            )
        }
    }

}


fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)
fun List<Post>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)


