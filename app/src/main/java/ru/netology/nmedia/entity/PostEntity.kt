package ru.netology.nmedia.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import ru.netology.nmedia.dto.Attachment
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.enum.AttachmentType

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val content: String,
    val likedByMe: Boolean,
    var likesCount: Long,
    val date: String,
    var shareCount: Long,
    var video: String? = null,
    val authorAvatar: String,
//    var attachment: Attachment?,
    var show: Boolean
) {

    fun toDto() = Post(
        id,
        author,
        content,
        likedByMe,
        likesCount,
        date,
        shareCount,
        video,
        authorAvatar,
//        attachment,
        show
    )

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(
                dto.id,
                dto.author,
                dto.content,
                dto.likedByMe,
                dto.likesCount,
                dto.date,
                dto.shareCount,
                dto.video,
                dto.authorAvatar,
//                dto.attachment,
                dto.show
            )
    }
}


fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)
fun List<Post>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)

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


