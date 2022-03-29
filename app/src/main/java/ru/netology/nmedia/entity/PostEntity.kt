package ru.netology.nmedia.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Attachment
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.enums.AttachmentType

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val authorId: Long,
    val content: String,
    val likedByMe: Boolean,
    var likesCount: Long,
    val date: String,
    var shareCount: Long,
    var video: String? = null,
    val authorAvatar: String,
//    var show: Boolean
    @Embedded
    var attachment: AttachmentEmbeddable?
) {

    fun toDto() = Post(
        id,
        author,
        authorId,
        content,
        likedByMe,
        likesCount,
        date,
        ownedByMe = false,
        shareCount,
        video,
        authorAvatar,
        attachment?.toDto()
    )

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(
                dto.id,
                dto.author,
                dto.authorId,
                dto.content,
                dto.likedByMe,
                dto.likesCount,
                dto.date,
                dto.shareCount,
                dto.video,
                dto.authorAvatar,
                AttachmentEmbeddable.fromDto(dto.attachment)
            )
    }
}

data class AttachmentEmbeddable(
    var url: String,
    var type: AttachmentType
) {
    fun toDto() = Attachment(url, type)

    companion object {
        fun fromDto(dto: Attachment?) = dto?.let {
            AttachmentEmbeddable(it.url, it.type)
        }
    }
}


fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)
fun List<Post>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)


